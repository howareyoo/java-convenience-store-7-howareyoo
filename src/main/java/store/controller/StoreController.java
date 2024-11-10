package store.controller;

import java.util.Map;
import store.model.Purchase;
import store.service.ProductService;
import store.service.PurchaseService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final PurchaseService purchaseService;
    private final InputView inputView;
    public final OutputView outputView;

    public StoreController(PurchaseService purchaseService, InputView inputView, OutputView outputView,
                           ProductService productService) {
        this.purchaseService = purchaseService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void processPurchase(Map<String, Integer> userPurchaseItem, boolean applyMembershipDiscount) {
        // 구매 세부 정보 가져오기
        Map<String, Purchase> purchaseDetails = purchaseService.calculateTotalOrigin(userPurchaseItem,
                applyMembershipDiscount);

        // 최종 영수증 출력
        printReceipt(purchaseDetails);
    }

    public void storeOpen() {
        boolean continuePurchasing = true;
        while (continuePurchasing) {
            try {
                // 1. 상품과 수량 입력 받기
                String userInput = inputView.readItem();
                Map<String, Integer> userPurchaseItem = purchaseService.parseProductInput(userInput);

                // 2. 프로모션 적용을 위한 수량 조정
                userPurchaseItem = purchaseService.adjustQuantitiesForPromotions(userPurchaseItem);

                // 3. 멤버십 할인 여부 입력 받기
                String membershipResponse = inputView.readMembershipApply();
                boolean applyMembershipDiscount = membershipResponse.equalsIgnoreCase("Y");

                // 4. 구매 확인 및 영수증 출력
                processPurchase(userPurchaseItem, applyMembershipDiscount);

                // 5. 추가 구매 여부 확인
                String additionalResponse = inputView.readAdditionalPurchases();
                continuePurchasing = additionalResponse.equalsIgnoreCase("Y");

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printReceipt(Map<String, Purchase> purchaseDetails) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");

        int totalQuantity = calculateAndPrintPurchaseItems(purchaseDetails);
        int totalOriginalPrice = calculateTotalOriginalPrice(purchaseDetails);
        int promotionDiscount = calculatePromotionDiscount(purchaseDetails);
        int membershipDiscount = calculateMembershipDiscount(purchaseDetails);

        printFreeItems(purchaseDetails);

        // 최종 결제 금액 계산
        int finalAmount = totalOriginalPrice - promotionDiscount - membershipDiscount;

        printSummary(totalQuantity, totalOriginalPrice, promotionDiscount, membershipDiscount, finalAmount);
    }

    private int calculateAndPrintPurchaseItems(Map<String, Purchase> purchaseDetails) {
        int totalQuantity = 0;
        for (Purchase purchase : purchaseDetails.values()) {
            System.out.println(purchase.getName() + "\t\t" + purchase.getQuantity() + "\t" + String.format("%,d",
                    purchase.getTotalPrice()));
            totalQuantity += purchase.getQuantity();
        }
        return totalQuantity;
    }

    private int calculateTotalOriginalPrice(Map<String, Purchase> purchaseDetails) {
        return purchaseDetails.values().stream()
                .mapToInt(Purchase::getTotalPrice)
                .sum();
    }

    private int calculatePromotionDiscount(Map<String, Purchase> purchaseDetails) {
        return purchaseDetails.values().stream()
                .mapToInt(Purchase::getPromotionTotalPrice)
                .sum();
    }

    private int calculateMembershipDiscount(Map<String, Purchase> purchaseDetails) {
        return purchaseDetails.values().stream()
                .mapToInt(Purchase::getMembershipDiscountPrice)
                .sum();
    }

    private void printFreeItems(Map<String, Purchase> purchaseDetails) {
        System.out.println("=============증    정===============");
        for (Purchase purchase : purchaseDetails.values()) {
            if (purchase.getPromotionQuantity() > 0) {
                System.out.println(purchase.getName() + "\t\t" + purchase.getPromotionQuantity());
            }
        }
    }

    private void printSummary(int totalQuantity, int totalOriginalPrice, int promotionDiscount, int membershipDiscount,
                              int finalAmount) {
        System.out.println("====================================");
        System.out.println("총구매액\t\t" + totalQuantity + "\t" + String.format("%,d", totalOriginalPrice));
        System.out.println("행사할인\t\t\t-" + String.format("%,d", promotionDiscount));
        System.out.println("멤버십할인\t\t\t-" + String.format("%,d", membershipDiscount));
        System.out.println("내실돈\t\t\t " + String.format("%,d", finalAmount));
    }

}
