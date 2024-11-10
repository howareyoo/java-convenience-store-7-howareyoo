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
            productService.checkLowStock(productName, quantity, productPromotionName);

            // 프로모션 적용 여부 확인
            boolean promotionApply = promotionService.checkPromotionApply(productPromotionName, quantity);

            if (!promotionApply) {
                // 프로모션 적용을 위해 필요한 추가 수량 계산
                int promotionNotApply = promotionService.checkPromotionNotApply(productPromotionName, quantity);

                // 사용자에게 추가 구매 의사 묻기
                String response = inputView.readPromotionApply(productName, promotionNotApply);

                if (response.equalsIgnoreCase("Y")) {
                    // 수량 증가
                    quantity += promotionNotApply;
                }
                // else: 사용자가 추가 구매를 원하지 않으면 수량 변경 없음
            }
            int lowStockPromotion = productService.checkLowStockPromotion(productName, quantity, productPromotionName);

            if (lowStockPromotion > 0) {
                String response = inputView.readPromotionRegularPrice(productName, lowStockPromotion);

                if (response.equalsIgnoreCase("N")) {
                    throw new IllegalArgumentException("[ERROR]사용자가 정가 구매를 원하지 않습니다. 다시 입력해 주세요.");
                }
            }

            adjustedPurchaseItem.put(productName, quantity);
        }

        return adjustedPurchaseItem;
    }

    private List<String> parseItemDetail(String item) {
        item = item.trim();

        // 입력 형식 검증 및 대괄호 제거
        item = removeBracketsAndValidate(item);

        // 상품명과 수량으로 분리
        String[] itemDetail = splitItemDetail(item);

        // 수량 파싱 및 검증
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
            productService.checkPromotionQuantity(productName, productPromotionName, quantity);
        }

        return purchaseDetails;
    }

}
