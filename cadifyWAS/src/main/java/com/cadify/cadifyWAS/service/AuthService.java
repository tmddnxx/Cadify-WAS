package com.cadify.cadifyWAS.service;

import com.cadify.cadifyWAS.exception.CustomLogicException;
import com.cadify.cadifyWAS.exception.ExceptionCode;
import com.cadify.cadifyWAS.model.dto.AuthDTO;
import com.cadify.cadifyWAS.model.entity.Token;
import com.cadify.cadifyWAS.repository.TokenRepository;
import com.cadify.cadifyWAS.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        UserDetails userDetails = authenticateMember(request.getEmail(), request.getPassword());
        // 기존의 Token 존재시 삭제
        tokenRepository.deleteByEmail(userDetails.getUsername());
        // Token 생성
        AuthDTO.AuthResponse response = generateTokenPair(userDetails.getUsername());
        // RefreshToken 저장
        tokenRepository.save(
                Token.builder()
                        .email(userDetails.getUsername())
                        .refreshToken(response.getRefreshToken())
                        .build()
        );
        return response;
    }

    @Transactional
    public void logout() {
        tokenRepository.deleteByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    @Transactional
    public AuthDTO.AuthResponse refresh(AuthDTO.RefreshTokenRequest request) {
        Claims claims = jwtProvider.validateToken(request.getRefreshToken());
        // Refresh Token이 만료되었는지 확인
        if (claims == null) {
            throw new CustomLogicException(ExceptionCode.TOKEN_IS_EXPIRED);
        } else {
            // 요청으로 받은 Token 에서 사용자 이메일을 추출,
            // 이메일에 해당하는 Refresh Token이 존재하는지 확인,
            // 이메일로 조회한 토큰 정보와 요청으로 받은 토큰이 일치하는지 확인.
            Token token = tokenRepository.findByEmail(claims.getSubject());
            if (token == null || isSameToken(token.getRefreshToken(), request.getRefreshToken())) {
                throw new CustomLogicException(ExceptionCode.INVALID_TOKEN);
            }else {
                // 새로운 token 세트 제공
                return generateTokenPair(token.getEmail());
            }
        }
    }

    private AuthDTO.AuthResponse generateTokenPair(String email) {
        String accessToken = jwtProvider.generateAccessToken(email);
        String refreshToken = jwtProvider.generateRefreshToken(email);

        return AuthDTO.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserDetails authenticateMember(String email, String password) {
        try {
            // loadUserByUsername 호출 & 내부적으로 구현된 PasswordEncoder를 사용하여 비밀번호 비교
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            return (UserDetails) authentication.getPrincipal();
        } catch (BadCredentialsException e) {
            throw new CustomLogicException(ExceptionCode.WRONG_LOGIN_INFO);
        }
    }

    private boolean isSameToken(String token, String validToken){
        if(Objects.equals(token, validToken)){
            return true;
        }else{
            throw new CustomLogicException(ExceptionCode.TOKEN_IS_EXPIRED);
        }
    }
}
