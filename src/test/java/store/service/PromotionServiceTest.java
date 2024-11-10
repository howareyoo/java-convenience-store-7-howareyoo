package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.DateTimes;
import java.net.URISyntaxException;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
    void isPromotionActive() throws URISyntaxException {
        String fileName = "promotions.md";
        PromotionRepository promotionRepository = new PromotionRepository(fileName);
        PromotionService promotionService = new PromotionService(promotionRepository);

        String promotionName = "탄산2+1";
        LocalDate currentDate = LocalDate.from(DateTimes.now());

        boolean promotionActive = promotionService.isPromotionActive(promotionName, currentDate);
        Assertions.assertTrue(promotionActive);

    }
}