package com.cadify.cadifyWAS.model.entity;

import com.cadify.cadifyWAS.model.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(nullable = false)
    private String memberName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    public Member updateMemberInfo(MemberDTO.Patch patch){

        Optional.ofNullable(patch.getMemberName()).ifPresent(
                name -> this.memberName = name);

        return this;
    }

}
