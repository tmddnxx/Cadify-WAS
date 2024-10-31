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
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.SignatureException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public MemberDTO.Response insertMember(MemberDTO.Post post) {
        if (memberRepository.findByEmail(post.getEmail()).isPresent()) {
            throw new CustomLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        } else {
            String encodedPassword = passwordEncoder.encode(post.getPassword());
            post.replacePassword(encodedPassword);
            Member member = memberMapper.memberPostToMember(post);

            return memberMapper.memberToMemberResponse(memberRepository.save(member));
        }
    }

    @Transactional
    public MemberDTO.Response updateMember(MemberDTO.Patch patch, HttpServletRequest request) {

        Member member = verifyEmail(jwtProvider.resolveToken(request));

        verifyPassword(patch.getPassword(), member.getPassword());

        return memberMapper.memberToMemberResponse(
                memberRepository.save(member.updateMemberInfo(patch)));
    }

    public String deleteMember(String password, HttpServletRequest request) {
        Member member = verifyEmail(jwtProvider.resolveToken(request));

        verifyPassword(password, member.getPassword());

        return "탈퇴 완료.";
    }

    public void verifyPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomLogicException(ExceptionCode.WRONG_PASSWORD); 
        }
    }

    public Member verifyEmail(String token) {
        String email = jwtProvider.extractUsername(token);
        Optional<Member> verifyMember = memberRepository.findByEmail(email);
        if (verifyMember.isPresent()) {
            return verifyMember.get();
        } else {
            throw new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
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
