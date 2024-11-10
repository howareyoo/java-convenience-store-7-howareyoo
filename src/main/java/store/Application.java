package store;

import store.controller.StoreController;

public class Application {
    public static void main(String[] args) {
        try {
            // 파일명 설정
            String productFileName = "products.md";
            String promotionFileName = "promotions.md";

            // AppConfig를 사용하여 애플리케이션 초기화
            store.config.AppConfig appConfig = new store.config.AppConfig(productFileName, promotionFileName);

            // StoreController 생성
            StoreController storeController = appConfig.storeController();

            // 상품 목록 출력
            storeController.outputView.printProducts();

            // 구매 프로세스 시작
            storeController.storeOpen();
        } catch (Exception e) {
            System.out.println("[ERROR] 프로그램 실행 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}
