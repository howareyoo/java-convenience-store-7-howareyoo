package store.model;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    private String getQuantityDisplay() {
        if (quantity == 0) {
            return "재고 없음";
        }
        return quantity + "개";
    }

    private String getPromotionDisplay() {
        if (promotion.equals("null")) {
            return "";
        }
        return promotion;
    }

    @Override
    public String toString() {
        String priceFommat = String.format("%,d원", price);
        String quantityDisplay = getQuantityDisplay();
        String promotionDisplay = getPromotionDisplay();
        return String.join(" ", "-", name, priceFommat, quantityDisplay, promotionDisplay);
    }

    public boolean hasName(String productName) {
        return this.name.equalsIgnoreCase(productName);
    }

    public boolean hasQuantity(int requestedQuantity) {
        return this.quantity >= requestedQuantity;
    }
}
