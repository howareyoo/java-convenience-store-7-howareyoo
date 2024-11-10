package store.model;

public class Promotion {
    private String name;
    private int buyQuantity;
    private String startDate;
    private String endDate;

    public Promotion(String name, int buyQuantity, String startDate, String endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
