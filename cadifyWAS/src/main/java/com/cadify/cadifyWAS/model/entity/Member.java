package com.cadify.cadifyWAS.model.entity;

import com.cadify.cadifyWAS.model.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    public enum Role{
        ADMIN,
        USER,
        COMPANY,
        FACTORY
    }
    public enum ConsentType{
        Marketing("마케팅 수신 동의"),
        EMAIL("이메일 수신 동의"),
        PHONE("휴대폰 수신 동의");

        private final String description;

        ConsentType(String description){
            this.description = description;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(nullable = false)
    private String memberName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String phone;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String memberRole;
    @Column
    private String company;
    @Column
    private String owner;
    @Column
    private String companyAddress;

    @ElementCollection
    @CollectionTable(name = "consents", joinColumns = @JoinColumn(name = "member_id"))
    @MapKeyColumn(name = "consent_type")
    @Column(name = "consent_status")
    @Enumerated(EnumType.STRING)
    private Map<ConsentType, Boolean> consents = new HashMap<>();

    public Member updateMemberInfo(MemberDTO.Patch patch) {

        Optional.ofNullable(patch.getMemberName()).ifPresent(
                name -> this.memberName = name);

        return this;
    }
}
