package store.repository;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import store.model.Product;
import store.utils.ItemUtil;

public class ProductRepository {
    private String fileName;

    public ProductRepository(String fileName) {
        this.fileName = fileName;
    }

    public List<Product> getProducts() throws URISyntaxException {
        List<Product> products = new ArrayList<>();
        List<String> productLines = ItemUtil.curProcuts(fileName);

        if (!productLines.isEmpty()) {
            productLines.remove(0);
        }

        for (String line : productLines) {
            products.add(getProduct(line));
        }
        return products;
    }

    private Product getProduct(String line) {
        String[] split = line.split(",");

        String name = split[0];
        int price = Integer.parseInt(split[1]);
        int quantity = Integer.parseInt(split[2]);
        String promotion = split[3];
        return new Product(name, price, quantity, promotion);
    }
}
