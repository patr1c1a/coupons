package com.challenge.coupons.service;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.boot.test.context.SpringBootTest;
import com.challenge.coupons.model.CouponRequest;
import com.challenge.coupons.model.CouponResponse;
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
        //mocked getItemPrice
        when(mercadoLibreApiService.getItemPrice("MLA1", "mockAccessToken")).thenReturn(100.0);
        when(mercadoLibreApiService.getItemPrice("MLA2", "mockAccessToken")).thenReturn(210.0);
        when(mercadoLibreApiService.getItemPrice("MLA3", "mockAccessToken")).thenReturn(260.0);
        when(mercadoLibreApiService.getItemPrice("MLA4", "mockAccessToken")).thenReturn(80.0);
        when(mercadoLibreApiService.getItemPrice("MLA5", "mockAccessToken")).thenReturn(90.0);

        //mocked getItemStatus
        when(mercadoLibreApiService.getItemStatus("MLA1", "mockAccessToken")).thenReturn("active");
        when(mercadoLibreApiService.getItemStatus("MLA2", "mockAccessToken")).thenReturn("active");
        when(mercadoLibreApiService.getItemStatus("MLA3", "mockAccessToken")).thenReturn("active");
        when(mercadoLibreApiService.getItemStatus("MLA4", "mockAccessToken")).thenReturn("active");
        when(mercadoLibreApiService.getItemStatus("MLA5", "mockAccessToken")).thenReturn("active");

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
     * Tests calculation of items when some favorite items are not active.
     */
    @Test
    public void testCalculateCouponItems_notActive() {
        //mocked getItemPrice
        when(mercadoLibreApiService.getItemPrice("MLA1", "mockAccessToken")).thenReturn(100.0);
        when(mercadoLibreApiService.getItemPrice("MLA2", "mockAccessToken")).thenReturn(210.0);
        when(mercadoLibreApiService.getItemPrice("MLA3", "mockAccessToken")).thenReturn(260.0);
        when(mercadoLibreApiService.getItemPrice("MLA4", "mockAccessToken")).thenReturn(80.0);
        when(mercadoLibreApiService.getItemPrice("MLA5", "mockAccessToken")).thenReturn(90.0);

        //mocked getItemStatus
        when(mercadoLibreApiService.getItemStatus("MLA1", "mockAccessToken")).thenReturn("active");
        when(mercadoLibreApiService.getItemStatus("MLA2", "mockAccessToken")).thenReturn("paused");
        when(mercadoLibreApiService.getItemStatus("MLA3", "mockAccessToken")).thenReturn("active");
        when(mercadoLibreApiService.getItemStatus("MLA4", "mockAccessToken")).thenReturn("paused");
        when(mercadoLibreApiService.getItemStatus("MLA5", "mockAccessToken")).thenReturn("active");

        couponService.setAccessToken("mockAccessToken");

        CouponRequest request = new CouponRequest();
        request.setItemIds(Arrays.asList("MLA1", "MLA2", "MLA3", "MLA4", "MLA5"));
        request.setCouponAmount(1000);

        CouponResponse response = couponService.calculateCouponItems(request);

        List<String> expectedItems = Arrays.asList("MLA5", "MLA1", "MLA3");
        double expectedTotalExpenditure = 450.0;

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
        //mocked getItemPrice
        when(mercadoLibreApiService.getItemPrice("MLA1", "mockAccessToken")).thenReturn(100.0);
        couponService.setAccessToken("mockAccessToken");
        
        //mocked getItemStatus
        when(mercadoLibreApiService.getItemStatus("MLA1", "mockAccessToken")).thenReturn("active");
        
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

    /**
     * Tests getting the top 5 favorited items.
     */
    @Test
    public void testGetTopFavoritedItems() {
        //mocked getFavoritedItemCounts
        couponService.getFavoritedItemCounts().put("MLA1", 15);
        couponService.getFavoritedItemCounts().put("MLA4", 9);
        couponService.getFavoritedItemCounts().put("MLA3", 7);
        couponService.getFavoritedItemCounts().put("MLA2", 6);
        couponService.getFavoritedItemCounts().put("MLA5", 3);

        Map<String, Integer> result = couponService.getTopFavoritedItems(3);

        Map<String, Integer> expected = Map.of(
                "MLA1", 15,
                "MLA4", 9,
                "MLA3", 7
        );

        assertEquals(expected, result);
    }
}