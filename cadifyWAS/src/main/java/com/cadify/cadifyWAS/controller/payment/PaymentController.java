package com.cadify.cadifyWAS.controller.payment;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.util.Base64;


@Log4j2
@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Value("${payment.toss.test_client_api_key}")
    private String tossClientApiKey;
    @Value("${payment.toss.test_secret_api_key}")
    private String tossSecretApiKey;

    @GetMapping("")
    public String payment(Model model){
        model.addAttribute("tossClientApiKey", tossClientApiKey);
        return "payment/main";
    }

    @GetMapping("/success") // 결제 요청 성공
    public String successURL(@RequestParam String paymentKey,
                             @RequestParam String orderId,
                             @RequestParam Long amount,
                             Model model) throws IOException, InterruptedException {

        log.info("---------성공 URL -------");
        log.info("PaymentKey :{} " , paymentKey);
        log.info("OrderID : {}" , orderId);
        log.info("Amount : {}" , amount);

        // 시크릿 키 인코딩
        String secretKey = tossSecretApiKey+":";
        String authorization = Base64.getEncoder().encodeToString(secretKey.getBytes());

        // 결제 승인 요청 보내기
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", "Basic " + authorization)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(String.format(
                        "{\"paymentKey\":\"%s\",\"orderId\":\"%s\",\"amount\":%d}",
                        paymentKey, orderId, amount)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Toss Payments Response: {}", response.body());

        if(response.statusCode() == 200) {
            // 결제 승인 성공
            // DB 저장
            return "payment/success";
        }else{
            //결제 승인 실패
            //CARD_NOT_SUPPORTED: 카드가 지원되지 않거나, 결제 방법으로 사용할 수 없는 경우.
            //CARD_EXPIRED: 카드의 유효기간이 만료된 경우.
            //INSUFFICIENT_BALANCE: 카드의 잔액이 부족한 경우.
            //INVALID_CARD_NUMBER: 카드 번호가 유효하지 않은 경우.
            //FRAUD_DETECTED: 결제가 사기로 의심되는 경우.

            model.addAttribute("error", "에러코드");
            return "payment/main"; // 오류페이지로 이동
        }




    }

    @GetMapping("/fail") // 결제 요청 실패
    public String failURL(@RequestParam("code") String code,
                          @RequestParam("message") String message,
                          @RequestParam("orderId") String orderId,
                          Model model){

        log.info("---------실패 URL -------");
        log.info("Code : {}" , code);
        log.info("Message {}: " , message);
        log.info("OrderID {}: " , orderId);

        return "payment/fail";
    }
}
