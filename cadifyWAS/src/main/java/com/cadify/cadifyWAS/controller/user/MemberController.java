package com.cadify.cadifyWAS.controller.user;

import com.cadify.cadifyWAS.Util.CustomAnnotation;
import com.cadify.cadifyWAS.model.dto.user.MemberDTO;
import com.cadify.cadifyWAS.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @CustomAnnotation.Member.SignUpOperation
    @PostMapping("/public/signup")
    public ResponseEntity<MemberDTO.Response> signUp(@Valid @RequestBody MemberDTO.Post post){

        return new ResponseEntity<>(
                memberService.insertMember(post),
                HttpStatus.OK);
    }
    @CustomAnnotation.Member.ModifyOperation
    @PatchMapping("/modify")
    public ResponseEntity<MemberDTO.Response> modify(
            @RequestBody MemberDTO.Patch patch,
            HttpServletRequest request){

        return new ResponseEntity<>(
                memberService.updateMember(patch, request),
                HttpStatus.OK);
    }
    @CustomAnnotation.Member.LeaveOperation
    @DeleteMapping("/leave")
    public ResponseEntity<String> leave(
            @RequestBody String password,
            HttpServletRequest request){

        return new ResponseEntity<>(
                memberService.deleteMember(password, request),
                HttpStatus.OK);
    }
}
