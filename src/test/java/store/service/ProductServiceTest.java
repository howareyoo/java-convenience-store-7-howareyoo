package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.model.Product;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

class ProductServiceTest {


    @Test
    void isProductExist() throws URISyntaxException {
        String fileName1 = "products.md";
        String fileName2 = "promotions.md";
        ProductRepository productRepository = new ProductRepository(fileName1);
        PromotionRepository promotionRepository = new PromotionRepository(fileName2);
        PromotionService promotionService = new PromotionService(promotionRepository);
        ProductService productService = new ProductService(productRepository, promotionService);

        boolean productExist = productService.isProductExist("콜라");
        assertTrue(productExist);

    }

    @Test
    void checkPromotion() throws URISyntaxException {
        String fileName1 = "products.md";
        String fileName2 = "promotions.md";
        ProductRepository productRepository = new ProductRepository(fileName1);
        PromotionRepository promotionRepository = new PromotionRepository(fileName2);
        PromotionService promotionService = new PromotionService(promotionRepository);
        ProductService productService = new ProductService(productRepository, promotionService);

        String productName = "콜라";
        String promotionName = "탄산2+1";
        int userPurchaseQuantity = 1;

        productService.checkPromotionQuantity(productName, promotionName, userPurchaseQuantity);
        // 예상 재고 확인
        List<Product> products = productRepository.getProducts();

        for (Product product : products) {
            if (product.getName().equals(productName) && product.getPromotion().equals(promotionName)) {
                assertNotNull(product);
                assertEquals(9, product.getQuantity());
            }
        }
    }
}