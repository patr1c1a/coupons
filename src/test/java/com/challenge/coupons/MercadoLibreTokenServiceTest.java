package com.challenge.coupons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MercadoLibreTokenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MercadoLibreTokenService tokenService;

    @Test
    public void testGetAccessToken_Success() {
        MercadoLibreTokenResponse mockResponse = new MercadoLibreTokenResponse();
        mockResponse.setAccessToken("mockAccessToken");
        mockResponse.setExpiresIn(21600);

        when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.eq(MercadoLibreTokenResponse.class)))
                .thenReturn(mockResponse);

        String accessToken = tokenService.getAccessToken();

        assertNotNull(accessToken);
    }
}
