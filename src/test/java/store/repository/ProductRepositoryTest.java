package store.repository;

import java.net.URISyntaxException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.model.Product;

class ProductRepositoryTest {

    @Test
    void getProducts() throws URISyntaxException {
        String fileName = "products.md";
        ProductRepository productRepository = new ProductRepository(fileName);
        List<Product> products = productRepository.getProducts();
        Assertions.assertThat(products).hasSize(18);
    }

    @Test
    void updateProduct() throws URISyntaxException {
        String fileName = "products.md";
        ProductRepository productRepository = new ProductRepository(fileName);

        String productName = "물";
        int quantity = 1;
        productRepository.updateProductQuantity(productName, quantity);
        List<Product> products = productRepository.getProducts();

        for (Product product : products) {
            if (product.getName().equals(productName)) {
                Assertions.assertThat(product.getQuantity()).isEqualTo(9);
            }
        }

    }
}