package com.cadify.cadifyWAS.mapper;

import com.cadify.cadifyWAS.model.dto.MemberDTO;
import com.cadify.cadifyWAS.model.entity.Member;
//import lombok.RequiredArgsConstructor;
//import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member memberPostToMember(MemberDTO.Post post){
        return Member.builder()
                .memberName(post.getMemberName())
                .email(post.getEmail())
                .password(post.getPassword())
                .addressNumber(post.getAddressNumber())
                .addressDetail(post.getAddressDetail())
                .phone(post.getPhone())
                .memberRole(post.getMemberRole())
                .consents(post.getConsents())
                .build();
    }
    public MemberDTO.Response memberToMemberResponse(Member member){
        return MemberDTO.Response.builder()
                .memberName(member.getMemberName())
                .email(member.getEmail())
                .memberRole(member.getMemberRole())
                .consents(member.getConsents())
                .build();
    }

//    public Member memberPatchToMember(MemberDTO.Patch patch){
//        return Member.builder()
//                .email(patch.getEmail())
//                .password(patch.getPassword())
//                .memberName(patch.getMemberName())
//                .build();
//    }
}
