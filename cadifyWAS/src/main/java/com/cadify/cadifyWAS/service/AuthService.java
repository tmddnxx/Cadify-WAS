package com.cadify.cadifyWAS.service;

import com.cadify.cadifyWAS.repository.TokenRepository;
import com.cadify.cadifyWAS.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService{

    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    public boolean validateToken(String token){
        return isTokenExpired(token);
    }

    public void deleteRefreshToken(String token){
        tokenRepository.deleteByRefreshToken(token);
    }

    private boolean isTokenExist(String token){
        return tokenRepository.existsByRefreshToken(token);
    }

    private boolean isTokenExpired(String token){
        if(jwtProvider.validateToken(token) == null){
            return false;
        }else{
            return true;
        }
    }
}
