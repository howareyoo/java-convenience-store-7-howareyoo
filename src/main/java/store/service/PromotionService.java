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

    public int getUnitQuantity(String promotionName) {
        List<Promotion> promotions = promotionRepository.getPromotions();
        return promotionRepository.findPromotion(promotionName, promotions)
                .map(Promotion::getPromotionUnit).orElse(0);
    }

    public boolean isPromotionActive(String promotionName, LocalDate currentDate) {
        Optional<Promotion> promotionOpt = promotionRepository.findPromotionByName(promotionName);
        boolean active = promotionOpt.map(promotion -> promotion.isPromotionActive(currentDate)).orElse(false);
        System.out.println("Promotion active status for " + promotionName + " on " + currentDate + ": " + active);
        return active;
    }


    public int calculatePromotionPrice(String promotionName, int unitPrice, int quantity) {
        Optional<Promotion> promotionOpt = promotionRepository.findPromotionByName(promotionName);

        if (promotionOpt.isPresent() && isPromotionActive(promotionName, LocalDate.now())) {
            Promotion promotion = promotionOpt.get();
            return promotion.calculatePriceWithPromotion(unitPrice, quantity);
        }

        return 0;
    }

    public boolean checkPromotionApply(String promotionName, int userPurchaseQuantity) {
        return isPromotionApply(promotionName, userPurchaseQuantity);
    }

    public int checkPromotionNotApply(String promotionName, int userPurchaseQuantity) {
        return getAddQuantityNeed(promotionName, userPurchaseQuantity);
    }

    public boolean isPromotionActiveOnDate(String productPromotionName, LocalDate currentDate) {
        return promotionRepository.findPromotionByName(productPromotionName)
                .map(promotion -> promotion.isPromotionActive(currentDate))
                .orElse(false); // 프로모션이 없거나 비활성 상태인 경우 false 반환
    }

    public int getApplicableUnitsIfActive(String productPromotionName, int quantity, LocalDate currentDate) {
        if (isPromotionActiveOnDate(productPromotionName, currentDate)) {
            return promotionRepository.findPromotionByName(productPromotionName)
                    .map(promotion -> promotion.getApplicableUnits(quantity))
                    .orElse(0);
        }
        return 0;
    }
}
