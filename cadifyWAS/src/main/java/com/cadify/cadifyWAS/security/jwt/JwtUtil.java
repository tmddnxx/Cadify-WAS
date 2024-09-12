package com.cadify.cadifyWAS.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration")
    private long expirationTime;
    // 토큰생성
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    // claim 추출(payload 부분)
    public Claims extractClaims(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)// 서명 검증
                .getBody();
    }

    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }
    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }
    public boolean validateToken(String token, String username){
        return(username.equals(extractUsername(token))) && !isTokenExpired(token);
    }
}
