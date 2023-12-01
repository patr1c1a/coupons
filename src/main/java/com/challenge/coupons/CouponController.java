package com.challenge.coupons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/coupon")
    public ResponseEntity<CouponResponse> getCouponItems(@RequestBody CouponRequest request) {
        if (request == null || request.getCouponAmount() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(couponService.calculateCouponItems(request));
    }
}