package store.model;

public class product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        String priceFommat = String.format("%,d원", price);
        return String.join(" ", name, priceFommat, quantity + "개", promotion);
    }


}
