package com.cadify.cadifyWAS.PaymentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReqDTO {
    private String payType; // 지불 방법
    private Long amount; // 가격
    private String orderName; // 주문 이름
    private String customerEmail; // 고객 이메일
    private String customerName; // 고객 이름

}
