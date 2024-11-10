package store.repository;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import store.model.Product;
import store.utils.ItemUtil;

public class ProductRepository {

    private final List<Product> products = new ArrayList<>();

    public ProductRepository(String fileName) throws URISyntaxException {
        loadProducts(fileName);
    }

    private void loadProducts(String fileName) throws URISyntaxException {
        List<String> productLines = ItemUtil.curProcuts(fileName);

        if (!productLines.isEmpty()) {
            productLines.remove(0);
        }

        for (String line : productLines) {
            products.add(getProduct(line));
        }
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);  // 리스트 복사본 반환 (수정 방지)
    }

    private Product getProduct(String line) {
        String[] split = line.split(",");

        String name = split[0];
        int price = Integer.parseInt(split[1]);
        int quantity = Integer.parseInt(split[2]);
        String promotion = split[3];
        return new Product(name, price, quantity, promotion);
    }

    public void updateProductQuantity(String productName, int quantity) throws URISyntaxException {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                product.setQuantity(product.getQuantity() - quantity);
                break;
            }
        }
    }


}
