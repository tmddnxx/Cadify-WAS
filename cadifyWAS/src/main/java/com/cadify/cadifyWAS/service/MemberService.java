package com.cadify.cadifyWAS.service;

import com.cadify.cadifyWAS.exception.CustomLogicException;
import com.cadify.cadifyWAS.exception.ExceptionCode;
import com.cadify.cadifyWAS.mapper.MemberMapper;
import com.cadify.cadifyWAS.model.dto.MemberDTO;
import com.cadify.cadifyWAS.model.entity.Member;
import com.cadify.cadifyWAS.repository.MemberRepository;
import com.cadify.cadifyWAS.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public MemberDTO.Response insertMember(MemberDTO.Post post){

        if(memberRepository.findByEmail(post.getEmail()).isPresent()){
            throw new CustomLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        }else{
            String encodedPassword = passwordEncoder.encode(post.getPassword());
            post.replacePassword(encodedPassword);
            Member member = memberMapper.memberPostToMember(post);

            return memberMapper.memberToMemberResponse(memberRepository.save(member));
        }
    }

    @Transactional
    public MemberDTO.Response updateMember(MemberDTO.Patch patch, HttpServletRequest request){
        Member member = verifyEmail(jwtProvider.resolveToken(request));
        if(verifyPassword(patch.getPassword(), member.getPassword())){
            return memberMapper.memberToMemberResponse(
                    memberRepository.save(member.updateMemberInfo(patch)));
        }else{
            throw new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    public String deleteMember(String password, HttpServletRequest request){
        Member member = verifyEmail(jwtProvider.resolveToken(request));

        System.out.println(password);
        System.out.println(member.getPassword());

        if(verifyPassword(password, member.getPassword())){
            memberRepository.delete(member);
            return "Good Bye";
        }else{
            return "아이디 혹은 비밀번호가 다릅니다.";
        }
    }

//    public MemberDTO.Response selectMember(String email){
//        return memberMapper.memberToMemberResponse(
//                memberRepository.findByEmail(email).get());
//    }

    public boolean verifyPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Member verifyEmail(String token){
        String email = jwtProvider.extractUsername(token);
        return memberRepository.findByEmail(email).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getMemberRole().toString()));

        return new User(
                member.getEmail(),
                member.getPassword(),
                authorities
        );
    }
}
