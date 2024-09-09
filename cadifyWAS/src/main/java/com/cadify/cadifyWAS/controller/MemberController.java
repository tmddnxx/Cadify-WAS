package com.cadify.cadifyWAS.controller;

import com.cadify.cadifyWAS.mapper.MemberMapper;
import com.cadify.cadifyWAS.model.dto.MemberDTO;
import com.cadify.cadifyWAS.model.entity.Member;
import com.cadify.cadifyWAS.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody MemberDTO.Post post){
        return null;
    }
    @PatchMapping("/modify")
    public ResponseEntity modify(@RequestBody MemberDTO.Patch patch){
        return null;
    }
    @GetMapping("/{email}")
    public ResponseEntity find( @PathVariable String email){
        return null;
    }
}
