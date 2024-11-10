package store.model;

public class Purchase {
    private String name;
    private int quantity;
    private int unitPrice;
    private int membershipDiscountPrice;
    private int promotionQuantity;
    private int promotionTotalPrice;


    public Purchase(String name, int quantity, int unitPrice, int membershipDiscountPrice, int promotionQuantity,
                    int promotionTotalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.membershipDiscountPrice = membershipDiscountPrice;
        this.promotionQuantity = promotionQuantity;
        this.promotionTotalPrice = promotionTotalPrice;

    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getMembershipDiscountPrice() {
        return membershipDiscountPrice;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    public int getPromotionTotalPrice() {
        return promotionTotalPrice;
    }

    // 총 구매 금액 계산
    public int getTotalPrice() {
        return unitPrice * quantity;
    }


}
