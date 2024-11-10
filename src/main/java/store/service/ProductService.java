package store.service;

import java.net.URISyntaxException;
import store.repository.ProductRepository;

public class ProductService {

    private final ProductRepository productRepository;
    private final PromotionService promotionService;

    public ProductService(ProductRepository productRepository, PromotionService promotionService) {
        this.productRepository = productRepository;
        this.promotionService = promotionService;
    }

    public boolean isProductExist(String productName) {
        return productRepository.getProducts().stream().anyMatch(product -> product.hasName(productName));
    }

    public void checkPromotionQuantity(String productName, String promotionName, int userPurchaseQuantity)
            throws URISyntaxException {
        boolean promotionApply = promotionService.isPromotionApply(productName, userPurchaseQuantity);

        if (promotionApply) {
            productRepository.updatePromotionQuantity(productName, promotionName, userPurchaseQuantity);
        }

        if (!promotionApply) {
            productRepository.updateProductQuantity(productName, userPurchaseQuantity);
        }
    }

}
