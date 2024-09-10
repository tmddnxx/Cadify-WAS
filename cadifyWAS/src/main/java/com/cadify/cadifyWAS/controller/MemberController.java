package com.cadify.cadifyWAS.controller;

import com.cadify.cadifyWAS.model.dto.MemberDTO;
import com.cadify.cadifyWAS.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody MemberDTO.Post post){
        return new ResponseEntity<>(
                memberService.insertMember(post),
                HttpStatus.OK);
    }
    @PatchMapping("/modify")
    public ResponseEntity modify(@RequestBody MemberDTO.Patch patch){
        return new ResponseEntity<>(
                memberService.updateMember(patch),
                HttpStatus.OK);
    }
    @GetMapping("/lookup/{email}")
    public ResponseEntity lookUp( @PathVariable String email){
        return new ResponseEntity<>(
                memberService.selectMember(email),
                HttpStatus.OK);
    }

    @DeleteMapping("/leave")
    public ResponseEntity leave(@RequestBody MemberDTO.Patch patch ){

        return new ResponseEntity<>(
                memberService.deleteMember(patch),
                HttpStatus.OK);
    }
}
