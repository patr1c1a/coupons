package com.challenge.coupons.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.coupons.model.CouponRequest;
import com.challenge.coupons.model.CouponResponse;
import com.challenge.coupons.service.CouponService;


/**
 * Controller that handles calls to /coupon.
 */
@RestController
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * Handles POST calls to /coupon endpoint.
     *
     * @param request The coupon request containing item_ids and coupon amount.
     * @return ResponseEntity<CouponResponse> with the endpoint response.
     */
    @PostMapping("/coupon")
    public ResponseEntity<CouponResponse> getCouponItems(@RequestBody CouponRequest request) {
        if (request == null || request.getCouponAmount() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(couponService.calculateCouponItems(request));
    }
}