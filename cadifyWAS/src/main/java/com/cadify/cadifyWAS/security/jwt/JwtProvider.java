package com.cadify.cadifyWAS.security.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final long expirationTime;
    private final long expirationTimeRefreshToken;

    public JwtProvider(@Value("${jwt.secret}")String keyString,
                       @Value("${jwt.expiration}")long expirationTime){
        this.expirationTime = expirationTime;
        byte[] byteKey = Base64.getDecoder().decode(keyString);
        this.secretKey = new SecretKeySpec(byteKey, SignatureAlgorithm.HS512.getJcaName());
        this.expirationTimeRefreshToken = expirationTime * 48 * 7;
    }

    // 토큰생성
    public String generateAccessToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    public String generateRefreshToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeRefreshToken))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    // claim 추출(payload 부분)
    public Claims validateToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)// 서명 검증
                .getBody();
    }
    // username 추출
    public String extractUsername(String token){
        return validateToken(token).getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")){
            return authorization.substring(7);
        }
        return null;
    }
}
