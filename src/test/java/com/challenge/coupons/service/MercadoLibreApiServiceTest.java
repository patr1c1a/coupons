package com.challenge.coupons.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import com.challenge.coupons.dto.MercadoLibreItemResponse;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class MercadoLibreApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MercadoLibreApiService mercadoLibreApiService;

    @Test
    public void testGetItemPrice() {
        //mocked data
        String itemId = "MLA123";
        String accessToken = "mockAccessToken";
        double expectedPrice = 100.0;

        MercadoLibreItemResponse itemResponse = new MercadoLibreItemResponse();
        itemResponse.setPrice(expectedPrice);

        ResponseEntity<MercadoLibreItemResponse> responseEntity = new ResponseEntity<>(itemResponse, HttpStatus.OK);

        when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class), Mockito.eq(MercadoLibreItemResponse.class)))
                .thenReturn(responseEntity);

        // Call the method
        double actualPrice = mercadoLibreApiService.getItemPrice(itemId, accessToken);

        // Assertions
        assertEquals(expectedPrice, actualPrice, 0.001);  // Add a delta for double comparison
    }

    @Test
    public void testGetItemStatus() {
        //mocked data
        String itemId = "MLA123";
        String accessToken = "mockAccessToken";
        String expectedStatus = "active";

        MercadoLibreItemResponse itemResponse = new MercadoLibreItemResponse();
        itemResponse.setStatus(expectedStatus);

        ResponseEntity<MercadoLibreItemResponse> responseEntity = new ResponseEntity<>(itemResponse, HttpStatus.OK);

        when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class), Mockito.eq(MercadoLibreItemResponse.class)))
                .thenReturn(responseEntity);

        String actualStatus = mercadoLibreApiService.getItemStatus(itemId, accessToken);

        assertEquals(expectedStatus, actualStatus);
    }
}
