package store.model;

import java.time.LocalDate;

public class Promotion {
    private String name;
    private int buyQuantity;
    private int getQuantity;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyQuantity, int getQuantity, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 프로모션 이름 반환
    public String getName() {
        return name;
    }

    //프로모션 하는거에 맞춰서 잘 샀는지 확인 -> 이게 안맞으면 프로모션 상품 안내 1개 더가지고 오라고 안내 할 때 사용
    public boolean applyPromotion(int userPurchseQuantity) {
        int promotionUnit = buyQuantity + getQuantity;
        return userPurchseQuantity % promotionUnit == 0;
    }

    //구매할 때 프로모션 재고 부족시, 몇개 상품 정가 구매해야 한다고 알려줘야 할 때 사용
    public int getBuyQuantity(int userPurchseQuantity) {
        int promotionUnit = buyQuantity + getQuantity;

        int remainder = userPurchseQuantity % promotionUnit;
       
        if (remainder > 0 && remainder < buyQuantity) {
            return 0;
        }
        return promotionUnit - remainder;
    }

    //프로모션 날짜 확인 사용
    public boolean isPromotionActive(LocalDate currentDate) {
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "name='" + name + '\'' +
                ", buyQuantity=" + buyQuantity +
                ", getQuantity=" + getQuantity +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
