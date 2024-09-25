package com.cadify.cadifyWAS.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);

        if(token != null && jwtProvider.validateToken(token)){
            try{
                UserDetails userDetails = userDetailsService.loadUserByUsername(
                        jwtProvider.extractUsername(token));

                if(userDetails != null){
                    JwtAuthenticationToken authentication =
                            new JwtAuthenticationToken(userDetails, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch(UsernameNotFoundException e){
                logger.error("User not found: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "존재하지 않는 회원");
                return;
            }catch(Exception e){
                logger.error("Authentication error: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 오류");
                return;
            }
        }else{
            logger.info("유효하지 않거나 만료된 토큰: {}", token);
        }
        filterChain.doFilter(request, response);
    }
}
