package com.challenge.coupons;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;

import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    //valid input
    public void testGetCoupon_ValidInput_ReturnsExpectedResponse() throws Exception {
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
    //item amount if <= 0
    public void testGetCoupon_InvalidInput_ReturnsErrorStatus() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setCouponAmount(-1);

        request.setItemIds(Collections.emptyList());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}