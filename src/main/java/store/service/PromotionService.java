package store.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import store.model.Promotion;
import store.repository.PromotionRepository;

public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public Optional<Promotion> getPromotionForProduct(String productName) {
        List<Promotion> promotions = promotionRepository.getPromotions();
        return promotionRepository.findPromotion(productName, promotions);
    }

    public boolean isPromotionApply(String promotionName, int userPurchaseQuantity) {
        List<Promotion> promotions = promotionRepository.getPromotions();
        return promotionRepository.findPromotion(promotionName, promotions)
                .map(promotion -> promotion.applyPromotion(userPurchaseQuantity)).orElse(false);
    }

    public int getAddQuantityNeed(String promotionName, int userPurchaseQuantity) {
        List<Promotion> promotions = promotionRepository.getPromotions();
        return promotionRepository.findPromotion(promotionName, promotions)
                .map(promotion -> promotion.getBuyQuantity(userPurchaseQuantity)).orElse(0);
    }

    public boolean isPromotionActive(String promotionName, LocalDate currentDate) {
        Optional<Promotion> promotionOpt = promotionRepository.findPromotionByName(promotionName);
        return promotionOpt.map(promotion -> promotion.isPromotionActive(currentDate)).orElse(false);
    }

    public int calculatePromotionPrice(String promotionName, int unitPrice, int quantity) {
        Optional<Promotion> promotionOpt = promotionRepository.findPromotionByName(promotionName);

        if (promotionOpt.isPresent() && isPromotionActive(promotionName, LocalDate.now())) {
            Promotion promotion = promotionOpt.get();
            return promotion.calculatePriceWithPromotion(unitPrice, quantity);
        }

        return 0;
    }


    public int getApplicableUnitsIfActive(String productPromotionName, int quantity, LocalDate currentDate) {
        return promotionRepository.findPromotionByName(productPromotionName)
                .filter(promotion -> promotion.isPromotionActive(currentDate)) // 프로모션 활성 상태 필터링
                .map(promotion -> promotion.getApplicableUnits(quantity))
                .orElse(0); // 프로모션이 없거나 비활성 상태인 경우 0 반환
    }
}
