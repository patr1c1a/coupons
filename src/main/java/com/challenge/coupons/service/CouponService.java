package com.challenge.coupons.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.challenge.coupons.model.CouponRequest;
import com.challenge.coupons.model.CouponResponse;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Service for calculating coupon items based on given ids and available coupon amount.
 */
@Service
public class CouponService {

    @Autowired
    private MercadoLibreTokenService tokenService;

    @Autowired
    private MercadoLibreApiService mercadoLibreApiService;

    private String accessToken;

    private final Map<String, Integer> favoritedItemCounts = new HashMap<>();


    /**
     * Calculates which items should be added to maximize item amount while spending coupon.
     * Items that are not active are ignored.
     *
     * @param request The coupon request containing item_ids and coupon amount.
     * @return CouponResponse containing the selected item IDs and total expenditure.
     */
    public CouponResponse calculateCouponItems(CouponRequest request) {
        //get a valid access token
        if (accessToken == null) {
            accessToken = tokenService.getAccessToken();
        }

        //select only active items and remove duplicates
        List<String> itemIds = request.getItemIds().stream().distinct().collect(Collectors.toList());
        List<String> validItemIds = itemIds.stream()
                .filter(itemId -> isItemActive(itemId, accessToken))
                .collect(Collectors.toList());

        //sort items by price (asc) to maximize item amount while spending coupon
        validItemIds.sort(Comparator.comparingDouble(itemId -> mercadoLibreApiService.getItemPrice(itemId, accessToken)));
        double remainingCouponAmount = request.getCouponAmount();
        List<String> selectedItems = new ArrayList<>();
        for (String itemId : validItemIds) {
            double itemPrice = mercadoLibreApiService.getItemPrice(itemId, accessToken);
            if (remainingCouponAmount - itemPrice >= 0) {
                selectedItems.add(itemId);
                remainingCouponAmount -= itemPrice;
            }
            if (remainingCouponAmount == 0) {
                break;
            }
        }

        //increment favorited item counts
        for (String itemId : validItemIds) {
            favoritedItemCounts.put(itemId, favoritedItemCounts.getOrDefault(itemId, 0) + 1);
        }

        CouponResponse response = new CouponResponse();
        response.setItemIds(selectedItems);
        response.setTotalExpenditure(request.getCouponAmount() - remainingCouponAmount);
        return response;
    }


    /**
     * Checks if an item ID is valid (status is "active").
     *
     * @param itemId The item ID to check.
     * @return true if the item ID is valid, false otherwise.
     */
    public boolean isItemActive(String itemId, String accessToken) {
        try {
            String status = mercadoLibreApiService.getItemStatus(itemId, accessToken);
            return "active".equals(status);
        } catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            return !responseBody.contains("\"error\"");
        }
    }

    /**
     * Get the top favorited items.
     *
     * @param top The number of favorited items to return.
     * @return Map of top favorited items with their respective counts.
     */
    public Map<String, Integer> getTopFavoritedItems(int top) {
        return favoritedItemCounts.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(top)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Map<String, Integer> getFavoritedItemCounts() {
        return favoritedItemCounts;
    }
}