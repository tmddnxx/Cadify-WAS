package com.cadify.cadifyWAS.controller;

import com.cadify.cadifyWAS.Util.CustomAnnotation;
import com.cadify.cadifyWAS.model.dto.MemberDTO;
import com.cadify.cadifyWAS.security.jwt.JwtProvider;
import com.cadify.cadifyWAS.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @CustomAnnotation.Member.LoginOperation
    @PostMapping("/public/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO.AuthenticationPost loginPost){
        try{
            // loadUserByUsername 호출
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginPost.getEmail(), loginPost.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtProvider.generateAccessToken(userDetails.getUsername());

            return ResponseEntity.ok(new MemberDTO.AuthenticationResponse(token));

        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 올바르지 않습니다.");
        }
    }
    public ResponseEntity<?> logout(@RequestHeader String refreshToken){
        return null;
    }
    public ResponseEntity<?> refresh(){
        return null;
    }
}
