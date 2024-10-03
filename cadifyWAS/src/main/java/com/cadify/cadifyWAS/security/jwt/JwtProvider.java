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
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expirationTime;

    // 토큰생성
    public String generateToken(String username){
        byte[] secretBytes = Base64.getDecoder().decode(secretKey);
        SecretKey byteKey = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());


        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(byteKey, SignatureAlgorithm.HS256)
                .compact();
    }
    // claim 추출(payload 부분)
    public Claims extractClaims(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)// 서명 검증
                .getBody();
    }
    // username 추출
    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")){
            return authorization.substring(7);
        }
        return null;
    }

//    만료 시간 검증
//    public boolean isTokenExpired(Claims claims){
//        return claims.getExpiration().before(new Date());
//    }
}
