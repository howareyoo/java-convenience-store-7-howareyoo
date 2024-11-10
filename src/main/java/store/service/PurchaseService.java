package store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseService {

    private final ProductService productService;

    public PurchaseService(ProductService productService) {
        this.productService = productService;
    }

    public Map<String, Integer> parseProductInput(String userInput) {
        Map<String, Integer> userPurchaseItem = new HashMap<>();

        List<String> items = List.of(userInput.split(","));
        for (String item : items) {
            List<String> itemDetail = parseItemDetail(item);
            String productName = itemDetail.get(0);
            int quantity = Integer.parseInt(itemDetail.get(1));
            userPurchaseItem.put(productName, quantity);
        }
        return userPurchaseItem;
    }

    private List<String> parseItemDetail(String item) {
        item = item.trim().replace("[", "").replace("]", "");
        return List.of(item.split("-"));
    }
}
