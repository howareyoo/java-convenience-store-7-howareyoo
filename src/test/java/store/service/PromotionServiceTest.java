package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.DateTimes;
import java.net.URISyntaxException;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import store.model.Promotion;
import store.repository.PromotionRepository;

class PromotionServiceTest {

    @Test
    void isPromotionApply() throws URISyntaxException {
        String fileName = "promotions.md";
        PromotionRepository promotionRepository = new PromotionRepository(fileName);
        PromotionService promotionService = new PromotionService(promotionRepository);

        String promotionName = "MD추천상품";
        int userPurchaseQuantity = 2;

        boolean promotionApply = promotionService.isPromotionApply(promotionName, userPurchaseQuantity);
        Assertions.assertTrue(promotionApply);
    }

    @Test
    void getAddQuantityNeed() throws URISyntaxException {
        String fileName = "promotions.md";
        PromotionRepository promotionRepository = new PromotionRepository(fileName);
        PromotionService promotionService = new PromotionService(promotionRepository);

        String promotionName = "탄산2+1";
        int userPurchaseQuantity = 5;

        int addQuantityNeed = promotionService.getAddQuantityNeed(promotionName, userPurchaseQuantity);
        assertThat(addQuantityNeed).isEqualTo(1);

    }

    @Test
    void isPromotionActiveDuringValidPeriod() throws URISyntaxException {
        String fileName = "promotions.md";
        PromotionRepository promotionRepository = new PromotionRepository(fileName);
        PromotionService promotionService = new PromotionService(promotionRepository);

        String promotionName = "반짝할인";

        // 프로모션이 활성화되는 날짜로 설정 (2024년 11월 15일)
        LocalDate activeDate = LocalDate.of(2024, 11, 15);

        boolean promotionActive = promotionService.isPromotionActive(promotionName, activeDate);
        Assertions.assertTrue(promotionActive);
    }


    @Test
    void isPromotionInactiveBeforeStart() throws URISyntaxException {
        String fileName = "promotions.md";
        PromotionRepository promotionRepository = new PromotionRepository(fileName);
        PromotionService promotionService = new PromotionService(promotionRepository);

        String promotionName = "반짝할인";

        // 프로모션 시작 전 날짜로 설정 (2024년 10월 31일)
        LocalDate inactiveDate = LocalDate.of(2024, 10, 31);

        boolean promotionActive = promotionService.isPromotionActive(promotionName, inactiveDate);
        Assertions.assertFalse(promotionActive);
    }


    @Test
    void isPromotionInactiveAfterEnd() throws URISyntaxException {
        String fileName = "promotions.md";
        PromotionRepository promotionRepository = new PromotionRepository(fileName);
        PromotionService promotionService = new PromotionService(promotionRepository);

        String promotionName = "반짝할인";

        // 프로모션 종료 후 날짜로 설정 (2024년 12월 1일)
        LocalDate inactiveDate = LocalDate.of(2024, 12, 1);

        boolean promotionActive = promotionService.isPromotionActive(promotionName, inactiveDate);
        Assertions.assertFalse(promotionActive);
    }

    @Test
    void testIsPromotionActive() {
        // 시작 날짜와 종료 날짜 설정
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 30);

        Promotion promotion = new Promotion("반짝할인",1,1 ,startDate, endDate);

        // 현재 날짜가 시작 날짜와 종료 날짜 사이인 경우
        LocalDate testDateWithinRange = LocalDate.of(2024, 11, 15);
        Assertions.assertTrue(promotion.isPromotionActive(testDateWithinRange));

        // 현재 날짜가 시작 날짜 이전인 경우
        LocalDate testDateBeforeStart = LocalDate.of(2024, 10, 31);
        Assertions.assertFalse(promotion.isPromotionActive(testDateBeforeStart));

        // 현재 날짜가 종료 날짜 이후인 경우
        LocalDate testDateAfterEnd = LocalDate.of(2024, 12, 1);
        Assertions.assertFalse(promotion.isPromotionActive(testDateAfterEnd));

        // 현재 날짜가 시작 날짜와 같은 경우
        LocalDate testDateOnStart = LocalDate.of(2024, 11, 1);
        Assertions.assertTrue(promotion.isPromotionActive(testDateOnStart));

        // 현재 날짜가 종료 날짜와 같은 경우
        LocalDate testDateOnEnd = LocalDate.of(2024, 11, 30);
        Assertions.assertTrue(promotion.isPromotionActive(testDateOnEnd));
    }


}