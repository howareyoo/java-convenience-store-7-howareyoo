package store.repository;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public void updateProductQuantity(String productName, int quantity) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                checkStock(product, quantity);
                product.setQuantity(product.getQuantity() - quantity);
                break;
            }
        }
    }

    public void updatePromotionQuantity(String productName, String promotionName, int quantity) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName) &&
                    promotionName.equalsIgnoreCase(product.getPromotion())) {
                checkStock(product, quantity);
                product.setQuantity(product.getQuantity() - quantity);
                break;
            }
        }
    }


    public void checkStock(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public int checkPromotionStock(Product product, int quantity, int unitQuantity) {
        if (product.getQuantity() < quantity) {
            int applayValue;
            applayValue = product.getQuantity() % unitQuantity;
            return product.getQuantity() - applayValue;
        }
        return 0;
    }

    public Optional<Product> findProductByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .findFirst();
    }
}
