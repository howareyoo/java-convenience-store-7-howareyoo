package store.controller;

import java.net.URISyntaxException;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.MembershipService;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.view.InputView;

class StoreControllerTest {

    private static PurchaseService getPurchaseService(String fileName1) throws URISyntaxException {
        String fileName2 = "promotions.md";
        ProductRepository productRepository = new ProductRepository(fileName1);
        PromotionRepository promotionRepository = new PromotionRepository(fileName2);
        PromotionService promotionService = new PromotionService(promotionRepository);
        ProductService productService = new ProductService(productRepository, promotionService);
        MembershipService membershipService = new MembershipService();
        InputView inputView = new InputView();
        return new PurchaseService(productService, promotionService, membershipService, inputView);
    }

   /* @Test
    void processPurchase() throws URISyntaxException {

        String fileName1 = "products.md";
        PurchaseService purchaseService = getPurchaseService(fileName1);
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        StoreController storeController = new StoreController(purchaseService, inputView, outputView);

        String userInput = "[콜라-1],[사이다-3]";
        Map<String, Integer> stringIntegerMap = purchaseService.parseProductInput(userInput);
        boolean isMembership = true;
        storeController.processPurchase(stringIntegerMap, isMembership);
    }*/


}