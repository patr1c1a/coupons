package com.challenge.coupons;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
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

        //select only items that are active
        List<String> itemIds = request.getItemIds();
        List<String> validItemIds = new ArrayList<>();
        for (String itemId : itemIds) {
            if (isItemActive(itemId, accessToken)) {
                validItemIds.add(itemId);
            }
        }

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
    protected boolean isItemActive(String itemId, String accessToken) {
        return "active".equals(mercadoLibreApiService.getItemStatus(itemId, accessToken));
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}