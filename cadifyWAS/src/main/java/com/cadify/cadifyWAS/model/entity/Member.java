package com.cadify.cadifyWAS.model.entity;

import com.cadify.cadifyWAS.Util.BaseTimeEntity;
import com.cadify.cadifyWAS.model.dto.user.MemberDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Map;
import java.util.Optional;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    public enum Role{
        ADMIN,
        USER,
        COMPANY,
        FACTORY
    }
    public enum ConsentName{
        MARKETING("마케팅 수신 동의"),
        ALM_EMAIL("이메일 수신 동의"),
        ALM_PHONE("휴대폰 수신 동의");

        private final String description;

        ConsentName(String description){
            this.description = description;
        }
    }
    public enum ConsentStatus{
        AGREE,
        DISAGREE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;
    @Column(nullable = false)
    private String memberName;
    // 검증 필요
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    // 검증 필요
    @Column(nullable = false, unique = true)
    private String phone;
    // 검증 필요
    @Column(nullable = false)
    private int addressNumber;
    @Column(nullable = false)
    private String addressDetail;
    // 기업, 공장 회원 추가 저장
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role memberRole = null;
    @Column
    private String company = null;
    @Column
    private String owner = null;
    @Column
    private String companyAddress = null;
    // 서비스 이용 관련 동의여부 관리 테이블
    @ElementCollection
    @CollectionTable(name = "consents", joinColumns = @JoinColumn(name = "member_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Enumerated(EnumType.STRING)
    @MapKeyColumn(name = "consent_name")
    @Column(name = "consent_status")
    @Cascade(CascadeType.ALL)
    private Map<ConsentName, ConsentStatus> consents;

    public Member updateMemberInfo(MemberDTO.Patch patch) {

        Optional.ofNullable(patch.getMemberName()).ifPresent(
                name -> this.memberName = name);
        Optional.ofNullable(patch.getAddressNumber()).ifPresent(
                addressNumber -> this.addressNumber = addressNumber);
        Optional.ofNullable(patch.getAddressDetail()).ifPresent(
                addressDetail -> this.addressDetail = addressDetail);
        Optional.ofNullable(patch.getConsents()).ifPresent(
                consents -> this.consents = consents);

        return this;
    }
}
