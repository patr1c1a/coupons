package com.challenge.coupons;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CouponServiceTest {

    @Mock
    private MercadoLibreApiService mercadoLibreApiService;

    @InjectMocks
    private CouponService couponService;

    /**
     * Tests calculation of items when multiple favorite items are available.
     */
    @Test
    public void testCalculateCouponItems_multipleItems() {
        //mocked API response
        when(mercadoLibreApiService.getItemPrice("MLA1", "mockAccessToken")).thenReturn(100.0);
        when(mercadoLibreApiService.getItemPrice("MLA2", "mockAccessToken")).thenReturn(210.0);
        when(mercadoLibreApiService.getItemPrice("MLA3", "mockAccessToken")).thenReturn(260.0);
        when(mercadoLibreApiService.getItemPrice("MLA4", "mockAccessToken")).thenReturn(80.0);
        when(mercadoLibreApiService.getItemPrice("MLA5", "mockAccessToken")).thenReturn(90.0);
        couponService.setAccessToken("mockAccessToken");

        CouponRequest request = new CouponRequest();
        request.setItemIds(Arrays.asList("MLA1", "MLA2", "MLA3", "MLA4", "MLA5"));
        request.setCouponAmount(500);

        CouponResponse response = couponService.calculateCouponItems(request);

        List<String> expectedItems = Arrays.asList("MLA4", "MLA5", "MLA1", "MLA2");
        double expectedTotalExpenditure = 480.0;

        assertEquals(expectedItems, response.getItemIds());
        assertEquals(expectedTotalExpenditure, response.getTotalExpenditure());
    }

    /**
     * Tests calculation of items when no favorite items are provided.
     */
    @Test
    public void testCalculateCouponItems_empty() {
        //mocked access token
        couponService.setAccessToken("mockAccessToken");

        CouponRequest requestNoItems = new CouponRequest();
        requestNoItems.setItemIds(Collections.emptyList());
        requestNoItems.setCouponAmount(100);

        CouponResponse responseNoItems = couponService.calculateCouponItems(requestNoItems);

        double expectedTotalExpenditure = 0.0;

        assertEquals(Collections.emptyList(), responseNoItems.getItemIds());
        assertEquals(expectedTotalExpenditure, responseNoItems.getTotalExpenditure());
    }

    /**
     * Tests calculation of items when only one favorite item is provided.
     */
    @Test
    public void testCalculateCouponItems_oneItem() {
        //mocked API response
        when(mercadoLibreApiService.getItemPrice("MLA1", "mockAccessToken")).thenReturn(100.0);
        couponService.setAccessToken("mockAccessToken");
        
        CouponRequest request = new CouponRequest();
        request.setItemIds(Arrays.asList("MLA1"));
        request.setCouponAmount(500);

        CouponResponse response = couponService.calculateCouponItems(request);

        List<String> expectedItems = Arrays.asList("MLA1");
        double expectedTotalExpenditure = 100.0;

        assertEquals(expectedItems, response.getItemIds());
        assertEquals(expectedTotalExpenditure, response.getTotalExpenditure());
    }

    /**
     * Tests calculation of items when the coupon amount is not enough for any item in the list.
     */
    @Test
    public void testCalculateCouponItems_amountNotEnough() {
        //mocked API response
        when(mercadoLibreApiService.getItemPrice("MLA1", "mockAccessToken")).thenReturn(100.0);
        couponService.setAccessToken("mockAccessToken");
        
        CouponRequest request = new CouponRequest();
        request.setItemIds(Arrays.asList("MLA1"));
        request.setCouponAmount(50);

        CouponResponse response = couponService.calculateCouponItems(request);

        double expectedTotalExpenditure = 0.0;

        assertEquals(Collections.emptyList(), response.getItemIds());
        assertEquals(expectedTotalExpenditure, response.getTotalExpenditure());
    }
}