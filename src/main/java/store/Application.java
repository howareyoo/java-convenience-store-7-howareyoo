package store;

import store.controller.StoreController;

public class Application {
    public static void main(String[] args) {
        try {
            String productFileName = "products.md";
            String promotionFileName = "promotions.md";

            store.config.AppConfig appConfig = new store.config.AppConfig(productFileName, promotionFileName);

            StoreController storeController = appConfig.storeController();

            storeController.storeOpen();
        } catch (Exception e) {
            System.out.println("[ERROR] 프로그램 실행 중 오류가 발생했습니다");
        }
    }

}
