package store.repository;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.model.Promotion;
import store.utils.ItemUtil;

public class PromotionRepository {

    private String fileName;

    public PromotionRepository(String fileName) {
        this.fileName = fileName;
    }

    public List<Promotion> getPromotions() throws URISyntaxException {
        List<Promotion> promotions = new ArrayList<>();
        List<String> promotinLines = ItemUtil.curProcuts(fileName);

        if (!promotinLines.isEmpty()) {
            promotinLines.remove(0);
        }

        for (String line : promotinLines) {
            promotions.add(getPromotion(line));
        }
        return promotions;
    }

    private Promotion getPromotion(String line) {
        String[] split = line.split(",");

        String name = split[0];
        int buy = Integer.parseInt(split[1]);
        int get = Integer.parseInt(split[2]);
        LocalDate startDate = LocalDate.parse(split[3]);
        LocalDate endDate = LocalDate.parse(split[4]);
        return new Promotion(name, buy, get, startDate, endDate);
    }

    public Optional<Promotion> findPromotion(String productName, List<Promotion> promotions) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equalsIgnoreCase(productName))
                .findFirst();
    }

}
