package com.example.roenbeauty.payment.client;

import com.example.roenbeauty.payment.dto.PaymentConfirmRequestDto;
import com.example.roenbeauty.payment.dto.TossPaymentConfirmResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class TossPaymentClient {

    private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private static final String TOSS_CANCEL_URL = "https://api.tosspayments.com/v1/payments/%s/cancel";

    private final RestTemplate restTemplate;
    private final String secretKey;

    public TossPaymentClient(@Value("${toss.secret-key}") String secretKey) {
        this.restTemplate = new RestTemplate();
        this.secretKey = secretKey;
    }

    public TossPaymentConfirmResponseDto confirm(PaymentConfirmRequestDto requestDto) {
        HttpHeaders headers = createHeaders();

        HttpEntity<PaymentConfirmRequestDto> requestEntity =
                new HttpEntity<>(requestDto, headers);

        ResponseEntity<TossPaymentConfirmResponseDto> response = restTemplate.exchange(
                TOSS_CONFIRM_URL,
                HttpMethod.POST,
                requestEntity,
                TossPaymentConfirmResponseDto.class
        );

        return response.getBody();
    }

    public TossPaymentConfirmResponseDto cancel(String paymentKey, String cancelReason) {
        HttpHeaders headers = createHeaders();

        Map<String, String> requestBody = Map.of(
                "cancelReason", cancelReason
        );

        HttpEntity<Map<String, String>> requestEntity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<TossPaymentConfirmResponseDto> response = restTemplate.exchange(
                String.format(TOSS_CANCEL_URL, paymentKey),
                HttpMethod.POST,
                requestEntity,
                TossPaymentConfirmResponseDto.class
        );

        return response.getBody();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String auth = secretKey + ":";
        String encodedAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        headers.set("Authorization", "Basic " + encodedAuth);

        return headers;
    }
}