package com.cadify.cadifyWAS.controller;

import com.cadify.cadifyWAS.model.dto.MemberDTO;
import com.cadify.cadifyWAS.security.jwt.JwtProvider;
import com.cadify.cadifyWAS.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @Operation(
            summary = "로그인",
            description = "token 인증 X"
    )
    @ApiResponses(value ={
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.AuthenticationResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "잘못된 로그인 정보",
                    content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "test@example.com"),
            @Parameter(name = "password", description = "비밀번호", example = "Password!"),
    })
    @PostMapping("/public/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO.AuthenticationPost loginPost){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginPost.getEmail(), loginPost.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtProvider.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new MemberDTO.AuthenticationResponse(token));

        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 올바르지 않습니다.");
        }
    }

    @PostMapping("/public/signup")
    public ResponseEntity<MemberDTO.Response> signUp(@Valid @RequestBody MemberDTO.Post post){

        return new ResponseEntity<>(
                memberService.insertMember(post),
                HttpStatus.OK);
    }
    @PatchMapping("/modify")
    public ResponseEntity<MemberDTO.Response> modify(
            @RequestBody MemberDTO.Patch patch,
            HttpServletRequest request){

        return new ResponseEntity<>(
                memberService.updateMember(patch, request),
                HttpStatus.OK);
    }
//    @GetMapping("/lookup/{email}")
//    public ResponseEntity<MemberDTO.Response> lookUp( @PathVariable String email){
//
//        return new ResponseEntity<>(
//                memberService.selectMember(email),
//                HttpStatus.OK);
//    }

    @DeleteMapping("/leave")
    public ResponseEntity<String> leave(
            @RequestBody String password,
            HttpServletRequest request){

        return new ResponseEntity<>(
                memberService.deleteMember(password, request),
                HttpStatus.OK);
    }
}
