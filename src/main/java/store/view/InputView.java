package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String readItem() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    public String readMembershipApply() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return Console.readLine();
    }

    public String readPromotionRegularPrice(String item, int itemCount) {
        StringBuilder message = new StringBuilder();
        message.append("현재 ")
                .append(item)
                .append(itemCount)
                .append("개는 ")
                .append("프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까?? (Y/N)");
        System.out.println("message");

        return Console.readLine();
    }

    public String readPromotionApply(String item, int promotionQuantity) {
        StringBuilder message = new StringBuilder();
        message.append("현재 ")
                .append(item)
                .append("은(는) ")
                .append(promotionQuantity)
                .append("개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        System.out.println("message");

        return Console.readLine();
    }

    public String readAdditionalPurchases() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return Console.readLine();
    }

}
