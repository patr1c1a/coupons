package com.challenge.coupons.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.challenge.coupons.model.CouponRequest;
import com.challenge.coupons.model.CouponResponse;
import com.challenge.coupons.service.CouponService;


/**
 * Controller that handles calls related to coupons.
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


    /**
     * Handles GET calls to /top-favorited endpoint.
     *
     * @param top The number of top favorited items to retrieve. Dafults to 5 if this is not provided in the request.
     * @return ResponseEntity<Map<String, Integer>> with the endpoint response.
     */
    @GetMapping("/top-favorited")
    public ResponseEntity<Map<String, Integer>> getTopFavoritedItems(@RequestParam(name = "top", required = false, defaultValue = "5") int top) {
        if (top <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(couponService.getTopFavoritedItems(top));
    }
}