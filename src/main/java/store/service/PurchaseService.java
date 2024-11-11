package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.model.Purchase;
import store.view.InputView;

public class PurchaseService {

    private final ProductService productService;
    private final PromotionService promotionService;
    private final MembershipService membershipService;
    private final InputView inputView; // InputView 주입


    public PurchaseService(ProductService productService, PromotionService promotionService,
                           MembershipService membershipService, InputView inputView) {
        this.productService = productService;
        this.promotionService = promotionService;
        this.membershipService = membershipService;
        this.inputView = inputView;
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

    public Map<String, Integer> adjustQuantitiesForPromotions(Map<String, Integer> userPurchaseItem) {
        Map<String, Integer> adjustedPurchaseItem = new HashMap<>();

        for (Map.Entry<String, Integer> entry : userPurchaseItem.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            String productPromotionName = productService.getProductPromotion(productName);

            checkLowStock(productName, quantity, productPromotionName);
            quantity = applyPromotionIfNeeded(productName, productPromotionName, quantity);
            checkAndAdjustForRegularPrice(productName, productPromotionName, quantity);

            adjustedPurchaseItem.put(productName, quantity);
        }

        return adjustedPurchaseItem;
    }

    private void checkLowStock(String productName, int quantity, String productPromotionName) {
        productService.checkLowStock(productName, quantity, productPromotionName);
    }

    private int applyPromotionIfNeeded(String productName, String productPromotionName, int quantity) {
        if (shouldApplyPromotion(productPromotionName, quantity)) {
            int additionalQuantity = calculateAdditionalQuantity(productName, productPromotionName, quantity);
            quantity += additionalQuantity;
        }
        return quantity;
    }

    private boolean shouldApplyPromotion(String productPromotionName, int quantity) {
        boolean promotionApply = promotionService.checkPromotionApply(productPromotionName, quantity);
        boolean isPromotionActive = promotionService.isPromotionActive(productPromotionName, LocalDate.now());
        return !promotionApply && isPromotionActive;
    }

    private int calculateAdditionalQuantity(String productName, String productPromotionName, int quantity) {
        int promotionNotApply = promotionService.checkPromotionNotApply(productPromotionName, quantity);
        if (promotionNotApply > 0) {
            String response = inputView.readPromotionApply(productName, promotionNotApply);
            if (response.equalsIgnoreCase("Y")) {
                return promotionNotApply;
            }
        }
        return 0;
    }

    private void checkAndAdjustForRegularPrice(String productName, String productPromotionName, int quantity) {
        int lowStockPromotion = productService.checkLowStockPromotion(productName, quantity, productPromotionName);

        if (lowStockPromotion > 0) {
            String response = inputView.readPromotionRegularPrice(productName, lowStockPromotion);

            if (response.equalsIgnoreCase("N")) {
                throw new IllegalArgumentException("[ERROR] 사용자가 정가 구매를 원하지 않습니다. 다시 입력해 주세요.");
            }
        }
    }


    private List<String> parseItemDetail(String item) {
        item = item.trim();
        item = removeBracketsAndValidate(item);

        String[] itemDetail = splitItemDetail(item);

        String productName = itemDetail[0].trim();
        int quantity = parseAndValidateQuantity(itemDetail[1].trim());

        return List.of(productName, String.valueOf(quantity));
    }

    private String removeBracketsAndValidate(String item) {
        if (!item.startsWith("[") || !item.endsWith("]")) {
            throw new IllegalArgumentException("[ERROR] 입력 형식이 올바르지 않습니다. 다시 입력 해 주세요");
        }
        return item.substring(1, item.length() - 1);
    }

    private String[] splitItemDetail(String item) {
        String[] itemDetail = item.split("-");
        if (itemDetail.length != 2) {
            throw new IllegalArgumentException("[ERROR] 입력 형식이 올바르지 않습니다. 다시 입력 해 주세요");
        }
        return itemDetail;
    }

    private int parseAndValidateQuantity(String quantityStr) {
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 입력 형식이 올바르지 않습니다. 다시 입력 해 주세요");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 입력 형식이 올바르지 않습니다. 다시 입력 해 주세요");
        }

        return quantity;
    }

    public int calculateTotalPromotion(String productName, int price, int quantity) {
        String productPromotionName = productService.getProductPromotion(productName);
        return promotionService.calculatePromotionPrice(productPromotionName, price, quantity);
    }

    public Map<String, Purchase> calculateTotalOrigin(Map<String, Integer> userPurchaseItem, boolean isDiscount) {
        Map<String, Purchase> purchaseDetails = new HashMap<>();
        LocalDate currentDate = LocalDate.from(DateTimes.now());

        for (Map.Entry<String, Integer> entry : userPurchaseItem.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();

            Purchase purchase = createPurchaseDetail(productName, quantity, isDiscount, currentDate);
            purchaseDetails.put(productName, purchase);

            updatePromotionQuantity(productName, quantity);
        }

        return purchaseDetails;
    }

    private Purchase createPurchaseDetail(String productName, int quantity, boolean isDiscount, LocalDate currentDate) {
        String productPromotionName = productService.getProductPromotion(productName);
        int unitPrice = productService.getProductPrice(productName);
        int discountPrice = membershipService.applayMembershipDiscount(isDiscount, unitPrice);
        int calculateTotalPromotion = calculateTotalPromotion(productName, unitPrice, quantity);
        int promotionQuantity = getPromotionQuantity(productPromotionName, quantity, currentDate);

        System.out.println("productName = " + productName + ", promotionQuantity = " + promotionQuantity
                + ", calculateTotalPromotion = " + calculateTotalPromotion);

        return new Purchase(productName, quantity, unitPrice, discountPrice, promotionQuantity, calculateTotalPromotion);
    }

    private int getPromotionQuantity(String productPromotionName, int quantity, LocalDate currentDate) {
        return promotionService.getApplicableUnitsIfActive(productPromotionName, quantity, currentDate);
    }

    private void updatePromotionQuantity(String productName, int quantity) {
        String productPromotionName = productService.getProductPromotion(productName);
        productService.checkPromotionQuantity(productName, productPromotionName, quantity);
    }

}
