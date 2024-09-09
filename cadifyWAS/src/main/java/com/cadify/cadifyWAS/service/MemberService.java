package com.cadify.cadifyWAS.service;

import com.cadify.cadifyWAS.exception.BusinessLogicException;
import com.cadify.cadifyWAS.exception.ExceptionCode;
import com.cadify.cadifyWAS.mapper.MemberMapper;
import com.cadify.cadifyWAS.model.dto.MemberDTO;
import com.cadify.cadifyWAS.model.entity.Member;
import com.cadify.cadifyWAS.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional
    public Member insertMember(MemberDTO.Post post){
        if(validateMemberByEmail(post.getEmail()).isPresent()){
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        }else{
            return memberRepository.save(memberMapper.memberPostToMember(post));
        }
    }
    @Transactional
    public Member updateMember(MemberDTO.Patch patch){
        if(validateMemberByEmail(patch.getEmail()).isPresent()){
            // 비밀번호 비교 로직 ( 암호화 작업 우선 )
            return memberRepository.save(memberMapper.memberPatchToMember(patch));
        }else{
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    public MemberDTO.Response selectMember(String email){

        return memberMapper.memberToMemberResponse(validateMemberByEmail(email).get());
    }

    private Optional<Member> validateMemberByEmail(String email){
        return memberRepository.findByEmail(email);
    }
}
