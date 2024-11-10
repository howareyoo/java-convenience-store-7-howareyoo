package store.config;

import java.net.URISyntaxException;
import store.controller.StoreController;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.MembershipService;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.PurchaseService;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {
    private final String productFileName;
    private final String promotionFileName;

    public AppConfig(String productFileName, String promotionFileName) {
        this.productFileName = productFileName;
        this.promotionFileName = promotionFileName;
    }

    // 리포지토리 생성 메서드
    public ProductRepository productRepository() throws URISyntaxException {
        return new ProductRepository(productFileName);
    }

    public PromotionRepository promotionRepository() throws URISyntaxException {
        return new PromotionRepository(promotionFileName);
    }

    // 서비스 생성 메서드
    public PromotionService promotionService() throws URISyntaxException {
        return new PromotionService(promotionRepository());
    }

    public ProductService productService() throws URISyntaxException {
        return new ProductService(productRepository(), promotionService());
    }

    public MembershipService membershipService() {
        return new MembershipService();
    }

    public PurchaseService purchaseService() throws URISyntaxException {
        return new PurchaseService(productService(), promotionService(), membershipService(), inputView());
    }

    // 뷰 생성 메서드
    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }

    // 컨트롤러 생성 메서드
    public StoreController storeController() throws URISyntaxException {
        return new StoreController(purchaseService(), inputView(), outputView(), productService());
    }
}
