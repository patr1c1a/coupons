package com.challenge.coupons;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service for calculating coupon items based on given ids and available coupon amount.
 */
@Service
public class CouponService {

    /**
     * Calculates which items should be added to maximize coupon expenditure.
     * Empty item ids and items with unknown prices are ignored.
     *
     * @param request The coupon request containing item_ids and coupon amount.
     * @return CouponResponse containing the selected item IDs and total expenditure.
     */
    public CouponResponse calculateCouponItems(CouponRequest request) {
        List<String> itemIds = request.getItemIds();
        double remainingCouponAmount = request.getCouponAmount();

        List<String> selectedItems = new ArrayList<>();

        List<String> validItemIds = new ArrayList<>();
        for (String itemId : itemIds) {
            if (isValidItemId(itemId)) {
                validItemIds.add(itemId);
            }
        }

        validItemIds.sort(Comparator.comparingDouble(this::getItemPrice));
        for (String itemId : validItemIds) {
            double itemPrice = getItemPrice(itemId);
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
     * Checks if an item ID is valid (item is valid if a price other than 0.0 is found for it).
     *
     * @param itemId The item ID to check.
     * @return true if the item ID is valid, false otherwise.
     */
    private boolean isValidItemId(String itemId) {
        if (getItemPrice(itemId) == 0.0)
            return false;
        else
            return true;
    }


    /**
     * Gets the price of an item based on its ID.
     * Includes test data with example item prices.
     *
     * @param itemId The ID of the item.
     * @return The price of the item, or 0.0 if the ID is unknown.
     */
    private double getItemPrice(String itemId) {
        switch (itemId) {
            case "MLA1": return 100.0;
            case "MLA2": return 210.0;
            case "MLA3": return 260.0;
            case "MLA4": return 80.0;
            case "MLA5": return 90.0;
            default: return 0.0;
        }
    }
}