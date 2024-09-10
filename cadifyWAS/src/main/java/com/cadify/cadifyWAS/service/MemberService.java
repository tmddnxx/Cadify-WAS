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
    public MemberDTO.Response insertMember(MemberDTO.Post post){

        if(memberRepository.findByEmail(post.getEmail()).isPresent()){
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        }else{
            return memberMapper.memberToMemberResponse(
                    memberRepository.save(memberMapper.memberPostToMember(post)));
        }
    }

    @Transactional
    public MemberDTO.Response updateMember(MemberDTO.Patch patch){

        Optional<Member> member = memberRepository.findByEmail(patch.getEmail());

        if(member.isPresent()){
            return memberMapper.memberToMemberResponse(
                    memberRepository.save(member.get().updateMemberInfo(patch)));
        }else{
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    public String deleteMember(MemberDTO.Patch patch){

        Optional<Member> member = memberRepository.findByEmail(patch.getEmail());

        if(member.isPresent()){
            memberRepository.delete(member.get());
            return "Delete OK";
        }else{
            return "Who Are You ???";
        }
    }

    public MemberDTO.Response selectMember(String email){
        return memberMapper.memberToMemberResponse(
                memberRepository.findByEmail(email).get());
    }
}
