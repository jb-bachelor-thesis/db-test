package net.joshuabrandes.dbtest.boundary.model;

import java.time.LocalDateTime;

public class ItemDTO {

    private String name;

    private String category;

    private LocalDateTime createdAt;

    private Double price;

    private String status;

    public ItemDTO() {
    }

    public ItemDTO(String name, String category, LocalDateTime createdAt, Double price, String status) {
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
        this.price = price;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
