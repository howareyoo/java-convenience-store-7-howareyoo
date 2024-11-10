package store.service;

import java.net.URISyntaxException;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.view.InputView;

class PurchaseServiceTest {

    @Test
    void parseProductInput() throws URISyntaxException {
        String fileName1 = "products.md";
        String fileName2 = "promotions.md";
        PurchaseService purchaseService = getPurchaseService(fileName1, fileName2);

        String userInput = "[콜라-0],[사이다-3]";

        Map<String, Integer> stringIntegerMap = purchaseService.parseProductInput(userInput);
        Assertions.assertThat(stringIntegerMap).hasSize(2);
        Assertions.assertThat(stringIntegerMap.get("콜라")).isEqualTo(0);
        Assertions.assertThat(stringIntegerMap.get("사이다")).isEqualTo(3);
    }

    @Test
    void calculateTotalPurchase() throws URISyntaxException {
        String fileName1 = "products.md";
        String fileName2 = "promotions.md";
        PurchaseService purchaseService = getPurchaseService(fileName1, fileName2);

        String productName = "콜라";
        int price = 1000;
        int quantity = 3;

        int totalPurchase = purchaseService.calculateTotalPromotion(productName, price, quantity);
        Assertions.assertThat(totalPurchase).isEqualTo(2000);


    }

    private static PurchaseService getPurchaseService(String fileName1, String fileName2) throws URISyntaxException {
        ProductRepository productRepository = new ProductRepository(fileName1);
        PromotionRepository promotionRepository = new PromotionRepository(fileName2);
        PromotionService promotionService = new PromotionService(promotionRepository);
        ProductService productService = new ProductService(productRepository, promotionService);
        MembershipService membershipService = new MembershipService();
        InputView inputView = new InputView();
        return new PurchaseService(productService, promotionService, membershipService, inputView);
    }
}