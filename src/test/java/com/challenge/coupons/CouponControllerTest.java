package com.challenge.coupons;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
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
import com.challenge.coupons.controller.CouponController;
import com.challenge.coupons.model.CouponRequest;
import com.challenge.coupons.model.CouponResponse;
import com.challenge.coupons.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    //when valid input
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

    @Test
    //when coupon amount < 0
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
}