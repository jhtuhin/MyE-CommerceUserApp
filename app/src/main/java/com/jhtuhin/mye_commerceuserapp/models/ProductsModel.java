package com.jhtuhin.mye_commerceuserapp.models;

public class ProductsModel {
    private String productName;
    private String productID;
    private String category;
    private String productImageUrl;
    private double price;
    private String description;
    private boolean status;

    public ProductsModel() {
    }

    public ProductsModel(String productName, String productID, String category,
                         String productImageUrl, double price, String description,
                         boolean status) {
        this.productName = productName;
        this.productID = productID;
        this.category = category;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.description = description;
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
