package store.service;

import java.net.URISyntaxException;
import java.util.List;
import store.model.Promotion;
import store.repository.PromotionRepository;

public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public boolean isPromotionApply(String promotionName, int userPurchaseQuantity) throws URISyntaxException {
        List<Promotion> promotions = promotionRepository.getPromotions();
        return promotionRepository.findPromotion(promotionName, promotions)
                .map(promotion -> promotion.applyPromotion(userPurchaseQuantity)).orElse(false);
    }

    public int getAddQuantityNeed(String promotionName, int userPurchaseQuantity) throws URISyntaxException {
        List<Promotion> promotions = promotionRepository.getPromotions();
        return promotionRepository.findPromotion(promotionName, promotions)
                .map(promotion -> promotion.getBuyQuantity(userPurchaseQuantity)).orElse(0);
    }
}
