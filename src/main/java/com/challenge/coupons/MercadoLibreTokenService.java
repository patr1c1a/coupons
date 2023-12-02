package com.challenge.coupons;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * Service to manage the access token.
 */
@Service
public class MercadoLibreTokenService {

    @Value("${coupons.clientId}")
    private String clientId;

    @Value("${coupons.clientSecret}")
    private String clientSecret;

    @Value("${mercadolibre.baseUrl}")
    private String mercadoLibreApiUrl;

    private String accessToken;
    private LocalDateTime tokenExpiration;

    public String getAccessToken() {
        if (accessToken == null || isTokenExpired()) {
            refreshAccessToken();
        }
        return accessToken;
    }


    /**
     * Checks if the token is expired.
     */
    private boolean isTokenExpired() {
        return tokenExpiration == null || LocalDateTime.now().isAfter(tokenExpiration);
    }

    /**
     * Refresh the access token.
     */
    private void refreshAccessToken() {
        String tokenEndpoint = mercadoLibreApiUrl+"/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("grant_type", "client_credentials");
        requestParams.put("client_id", clientId);
        requestParams.put("client_secret", clientSecret);

        MercadoLibreTokenResponse response = restTemplate.postForObject(tokenEndpoint, requestParams, MercadoLibreTokenResponse.class);

        if (response != null) {
            accessToken = response.getAccessToken();
            int expiresIn = response.getExpiresIn();
            tokenExpiration = LocalDateTime.now().plusSeconds(expiresIn);
        }
    }
}