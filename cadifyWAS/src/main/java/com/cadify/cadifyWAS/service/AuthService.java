package com.cadify.cadifyWAS.service;

import com.cadify.cadifyWAS.exception.CustomLogicException;
import com.cadify.cadifyWAS.exception.ExceptionCode;
import com.cadify.cadifyWAS.model.dto.AuthDTO;
import com.cadify.cadifyWAS.model.entity.Token;
import com.cadify.cadifyWAS.repository.TokenRepository;
import com.cadify.cadifyWAS.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService{

    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request){
        // loadUserByUsername 호출 & 내부적으로 구현된 PasswordEncoder를 사용하여 비밀번호 비교
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        tokenRepository.deleteById(userDetails.getUsername());
        // token 생성
        String accessToken = jwtProvider.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtProvider.generateRefreshToken(userDetails.getUsername());

        tokenRepository.save(
                Token.builder()
                        .email(authentication.getName())
                        .refreshToken(refreshToken)
                        .build()
        );

        return AuthDTO.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Transactional
    public void logout(){
        Token token = tokenRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        tokenRepository.save(token.updateExpired());
    }

    @Transactional
    public String refresh(AuthDTO.RefreshTokenRequest request){
        Claims claims = jwtProvider.validateToken(request.getRefreshToken());
        if(claims == null){
            throw new CustomLogicException(ExceptionCode.TOKEN_IS_EXPIRED);
        }else{
            String email = claims.getSubject();
            if(!tokenRepository.existsByEmail(email)){
                throw new CustomLogicException(ExceptionCode.INVALID_TOKEN);
            }else{
                return jwtProvider.generateAccessToken(email);
            }
        }
    }
}
