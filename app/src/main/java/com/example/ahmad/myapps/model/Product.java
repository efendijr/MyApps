package com.example.ahmad.myapps.model;

import java.math.BigDecimal;

/**
 * Created by ahmad on 31/01/2016.
 */
public class Product {
    String id, name, description, image, sku, created_at;
    BigDecimal price;

    public Product(String id, String name, String description, String image, String sku, String created_at, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.sku = sku;
        this.created_at = created_at;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
