package net.joshuabrandes.dbtest.boundary.model;

public class CategoryDTO {

    private String category;

    private Double averagePrice;

    public CategoryDTO() {
    }

    public CategoryDTO(String category, Double averagePrice) {
        this.category = category;
        this.averagePrice = averagePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }
}
