package com.cadify.cadifyWAS.controller;

import com.cadify.cadifyWAS.model.dto.MemberDTO;
import com.cadify.cadifyWAS.security.jwt.JwtUtil;
import com.cadify.cadifyWAS.service.MemberService;
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

    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody MemberDTO.LoginPost loginPost){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginPost.getEmail(), loginPost.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new MemberDTO.LoginResponse(token));

        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 올바르지 않습니다.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberDTO.Response> signUp(@Valid @RequestBody MemberDTO.Post post){

        return new ResponseEntity<>(
                memberService.insertMember(post),
                HttpStatus.OK);
    }
    @PatchMapping("/modify")
    public ResponseEntity<MemberDTO.Response> modify(@RequestBody MemberDTO.Patch patch){

        return new ResponseEntity<>(
                memberService.updateMember(patch),
                HttpStatus.OK);
    }
    @GetMapping("/lookup/{email}")
    public ResponseEntity<MemberDTO.Response> lookUp( @PathVariable String email){

        return new ResponseEntity<>(
                memberService.selectMember(email),
                HttpStatus.OK);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<String> leave(@RequestBody MemberDTO.Patch patch ){

        return new ResponseEntity<>(
                memberService.deleteMember(patch),
                HttpStatus.OK);
    }
}
