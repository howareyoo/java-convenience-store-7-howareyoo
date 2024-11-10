package store.contents;

public enum MembershipDiscount {
    MEMBERSHIP_DISCOUNT(0.3f, 8000);

    private float discountRate;
    private int maxDiscount;

    MembershipDiscount(float discountRate, int maxDiscount) {
        this.discountRate = discountRate;
        this.maxDiscount = maxDiscount;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public int getMaxDiscount() {
        return maxDiscount;
    }
}
