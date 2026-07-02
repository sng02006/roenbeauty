package com.example.roenbeauty.user.client;

import com.example.roenbeauty.user.dto.KakaoTokenResponseDto;
import com.example.roenbeauty.user.dto.KakaoUserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    private final RestClient restClient;
    private final String clientId;
    private final String defaultRedirectUri;
    private final String tokenUri;
    private final String userInfoUri;
    private final String unlinkUri;
    private final String adminKey;

    public KakaoClient(
            @Value("${kakao.client-id}") String clientId,
            @Value("${kakao.redirect-uri}") String defaultRedirectUri,
            @Value("${kakao.token-uri}") String tokenUri,
            @Value("${kakao.user-info-uri}") String userInfoUri,
            @Value("${kakao.unlink-uri}") String unlinkUri,
            @Value("${kakao.admin-key}") String adminKey
    ) {
        this.restClient = RestClient.create();
        this.clientId = clientId;
        this.defaultRedirectUri = defaultRedirectUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
        this.unlinkUri = unlinkUri;
        this.adminKey = adminKey;
    }

    public KakaoTokenResponseDto getToken(String code, String redirectUri) {
        
        String finalRedirectUri = redirectUri == null || redirectUri.isBlank()
                ? defaultRedirectUri
                : redirectUri;

        System.out.println("clientId = " + clientId);
        System.out.println("redirectUri = " + finalRedirectUri);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", finalRedirectUri);
        body.add("code", code);

        return restClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(KakaoTokenResponseDto.class);
    }

    public KakaoUserResponseDto getUserInfo(String accessToken) {
        return restClient.get()
                .uri(userInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KakaoUserResponseDto.class);
    }

    public void unlinkByAdminKey(String providerId) {
        LinkedMultiValueMap<String, String> body =
                new LinkedMultiValueMap<>();

        body.add("target_id_type", "user_id");
        body.add("target_id", providerId);

        restClient.post()
                .uri(unlinkUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "KakaoAK " + adminKey)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}