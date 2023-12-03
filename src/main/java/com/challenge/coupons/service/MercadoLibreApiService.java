package com.challenge.coupons.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.challenge.coupons.dto.MercadoLibreItemResponse;


@Service
public class MercadoLibreApiService {

    @Value("${mercadolibre.baseUrl}")
    private String mercadoLibreApiUrl;

    private final String itemEndpoint = "/items/";

    private final RestTemplate restTemplate;

    public MercadoLibreApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double getItemPrice(String itemId, String accessToken) {
        String apiUrl = mercadoLibreApiUrl + itemEndpoint + itemId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<MercadoLibreItemResponse> responseEntity =
                restTemplate.exchange(apiUrl, HttpMethod.GET, entity, MercadoLibreItemResponse.class);

        MercadoLibreItemResponse itemResponse = responseEntity.getBody();
        return (itemResponse != null) ? itemResponse.getPrice() : -1.0;
    }

    public String getItemStatus(String itemId, String accessToken) {
        String apiUrl = mercadoLibreApiUrl + itemEndpoint + itemId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<MercadoLibreItemResponse> responseEntity =
                restTemplate.exchange(apiUrl, HttpMethod.GET, entity, MercadoLibreItemResponse.class);

        MercadoLibreItemResponse itemResponse = responseEntity.getBody();
        return (itemResponse != null) ? itemResponse.getStatus() : "invalid";
    }
}
