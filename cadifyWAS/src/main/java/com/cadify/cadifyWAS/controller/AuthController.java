package com.cadify.cadifyWAS.controller;

import com.cadify.cadifyWAS.Util.CustomAnnotation;
import com.cadify.cadifyWAS.model.dto.AuthDTO;
import com.cadify.cadifyWAS.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @CustomAnnotation.Member.LoginOperation
    @PostMapping("/public/login")
    public ResponseEntity<AuthDTO.AuthResponse> login(@RequestBody AuthDTO.LoginRequest loginRequest){
        return new ResponseEntity<>(
                authService.login(loginRequest),
                HttpStatus.OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        authService.logout();
        return new ResponseEntity<>(
                "로그아웃 하셨습니다.",
                HttpStatus.OK);
    }
    @PostMapping("/public/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthDTO.RefreshTokenRequest refreshTokenRequest){
        return new ResponseEntity<>(
                authService.refresh(refreshTokenRequest),
                HttpStatus.OK
        );
    }
}
