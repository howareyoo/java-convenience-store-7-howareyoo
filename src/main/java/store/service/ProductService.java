package store.service;

import store.model.Product;
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

    public void checkPromotionQuantity(String productName, String promotionName, int userPurchaseQuantity) {
        boolean promotionApply = promotionService.isPromotionApply(productName, userPurchaseQuantity);

        if (promotionApply) {
            productRepository.updatePromotionQuantity(productName, promotionName, userPurchaseQuantity);
        }

        if (!promotionApply) {
            productRepository.updateProductQuantity(productName, userPurchaseQuantity);
        }
    }

    // 제품 이름을 받아 가격을 반환하는 메서드
    public int getProductPrice(String productName) {
        return productRepository.findProductByName(productName)
                .map(Product::getPrice)
                .orElse(0);  // 제품을 찾지 못하면 0 반환 (필요시 예외 처리 가능)
    }

    public String getProductPromotion(String productName) {
        return productRepository.findProductByName(productName)
                .map(Product::getPromotion)
                .orElse("프로모션 없음");  // 제품이 없거나 프로모션이 없는 경우 "프로모션 없음" 반환
    }
}
