package com.cadify.cadifyWAS.Entity;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    private long memberId; // 사용자 식별ID
    private String payType; // 지불 방법
    private Long amount; // 가격
    private String orderId; // 주문 고유 ID
    private String orderName; // 주문 이름
    private String createDate; // 결제 날짜
    private boolean paySuccess; // 결제 성공 여부 (true or false)
    private boolean cancelSuccess; // 취소 성공 여부

}
