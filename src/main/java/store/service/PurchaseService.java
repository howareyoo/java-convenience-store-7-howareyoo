package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.model.Purchase;

public class PurchaseService {

    private final ProductService productService;
    private final PromotionService promotionService;
    private final MembershipService membershipService;

    public PurchaseService(ProductService productService, PromotionService promotionService,
                           MembershipService membershipService) {
        this.productService = productService;
        this.promotionService = promotionService;
        this.membershipService = membershipService;
    }

    public Map<String, Integer> parseProductInput(String userInput) {
        Map<String, Integer> userPurchaseItem = new HashMap<>();

        List<String> items = List.of(userInput.split(","));
        for (String item : items) {
            List<String> itemDetail = parseItemDetail(item);
            String productName = itemDetail.get(0);
            int quantity = Integer.parseInt(itemDetail.get(1));
            userPurchaseItem.put(productName, quantity);
        }
        return userPurchaseItem;
    }

    private List<String> parseItemDetail(String item) {
        item = item.trim().replace("[", "").replace("]", "");
        return List.of(item.split("-"));
    }

    public int calculateTotalPromotion(String productName, int price, int quantity) {
        String productPromotionName = productService.getProductPromotion(productName);
        System.out.println(productPromotionName);
        return promotionService.calculatePromotionPrice(productPromotionName, price, quantity);
    }

    public Map<String, Purchase> calculateTotalOrigin(Map<String, Integer> userPurchaseItem, boolean isDiscount) {
        Map<String, Purchase> purchaseDetails = new HashMap<>();

        for (Map.Entry<String, Integer> entry : userPurchaseItem.entrySet()) {
            String productName = entry.getKey();
            String productPromotionName = productService.getProductPromotion(productName);
            int quantity = entry.getValue();
            int unitPrice = productService.getProductPrice(productName);
            int discountPrice = membershipService.applayMembershipDiscount(isDiscount, unitPrice);
            int calculateTotalPromotion = calculateTotalPromotion(productName, unitPrice, quantity);
            LocalDate currentDate = LocalDate.from(DateTimes.now());
            int promotionQuantity = promotionService.getApplicableUnitsIfActive(productPromotionName, quantity,
                    currentDate);
            System.out.println("productName = " + productName + ", promotionQuantity = " + promotionQuantity
                    + " calculateTotalPromotion = " + calculateTotalPromotion);
            Purchase detail = new Purchase(productName, quantity, unitPrice, discountPrice, promotionQuantity,
                    calculateTotalPromotion);
            purchaseDetails.put(productName, detail);
        }

        return purchaseDetails;
    }

}
