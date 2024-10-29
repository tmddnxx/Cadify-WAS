package com.cadify.cadifyWAS.PaymentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResDTO {

    private String payType; // 지불 방법
    private Long amount; // 가격
    private String orderId; // 주문 고유 ID
    private String orderName; // 주문 이름
    private String customerEmail; // 고객 이메일
    private String customerName; // 고객 이름
    private String successUrl; // 성공시 콜백 주소
    private String failUrl; // 실패시 콜백 주소
    private String createDate; // 결제 날짜
    private String paySuccessYN; // 결제 성공 여부 (Y or N)
}
