package com.example.ahmad.myapps.model;

import java.math.BigDecimal;

/**
 * Created by ahmad on 3/31/16.
 */
public class Food {
    String id, name, description, photo;
    BigDecimal price;

    public Food(String id, String name, String description, String photo, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}