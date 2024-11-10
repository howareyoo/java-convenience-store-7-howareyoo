package store.view;

import java.net.URISyntaxException;
import java.util.List;
import store.model.Product;
import store.repository.ProductRepository;

public class OutputView {

    public void printProducts() throws URISyntaxException {
        System.out.println("안녕하세요. W편의점입니다.\n"
                + "현재 보유하고 있는 상품입니다.");
        String fileName = "products.md";
        ProductRepository productRepository = new ProductRepository(fileName);

        List<Product> products = productRepository.getProducts();
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
