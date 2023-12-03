package com.challenge.coupons.controller;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.challenge.coupons.model.CouponRequest;
import com.challenge.coupons.model.CouponResponse;
import com.challenge.coupons.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @InjectMocks
    private CouponController couponController;


    /**
     * POST to /coupon when input is valid.
     */
    @Test
    public void testGetCoupon_ValidInput_ReturnsExpectedResponse() throws Exception {
        //mock service layer
        Mockito.when(couponService.calculateCouponItems(Mockito.any(CouponRequest.class)))
                .thenReturn(new CouponResponse(Arrays.asList("MLA4", "MLA5", "MLA1", "MLA2"), 480.0));
        
        //mock MercadoLibreApiService
        Mockito.when(couponService.isItemActive(Mockito.eq("MLA1"), Mockito.anyString()))
                .thenReturn(true);

        CouponRequest request = new CouponRequest();
        request.setItemIds(Arrays.asList("MLA1", "MLA2", "MLA3", "MLA4", "MLA5"));
        request.setCouponAmount(500);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponse = "{\"itemIds\":[\"MLA4\",\"MLA5\",\"MLA1\",\"MLA2\"],\"totalExpenditure\":480.0}";
        String actualResponse = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedResponse, actualResponse, true);
    }


    /**
     * POST to /coupon when coupon amount < 0.
     */
    @Test
    public void testGetCoupon_InvalidInput_ReturnsErrorStatus() throws Exception {        
        //mock service layer
        Mockito.when(couponService.calculateCouponItems(Mockito.any(CouponRequest.class)))
                .thenReturn(new CouponResponse(Collections.emptyList(), 0.0));

        //mock MercadoLibreApiService
        Mockito.when(couponService.isItemActive(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(true);

        CouponRequest request = new CouponRequest();
        request.setItemIds(Arrays.asList("MLA1", "MLA2", "MLA3", "MLA4", "MLA5"));
        request.setCouponAmount(-1);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    /**
     * GET to /top-favorited to retrieve the top favorited items.
     */
    @Test
    public void testGetTopFavoritedItems_ReturnsExpectedResponse() throws Exception {
        //mock service layer
        Mockito.when(couponService.getTopFavoritedItems(Mockito.anyInt()))
                .thenReturn(Map.of(
                        "MLA1", 15,
                        "MLA4", 9,
                        "MLA3", 7,
                        "MLA2", 6,
                        "MLA5", 3
                ));
    
        MvcResult result = mockMvc.perform(get("/top-favorited")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    
        String expectedResponse = "{\"MLA1\":15,\"MLA4\":9,\"MLA3\":7,\"MLA2\":6,\"MLA5\":3}";
        String actualResponse = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedResponse, actualResponse, true);
    }
}