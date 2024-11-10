package store.service;

import java.net.URISyntaxException;
import store.repository.ProductRepository;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean isProductExist(String productName) throws URISyntaxException {
        return productRepository.getProducts().stream().anyMatch(product -> product.hasName(productName));
    }

}
