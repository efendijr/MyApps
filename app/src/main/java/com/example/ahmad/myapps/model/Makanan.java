package com.example.ahmad.myapps.model;

import java.math.BigDecimal;

/**
 * Created by ahmad on 02/02/2016.
 */
public class Makanan {
    String menu_id, menu_name, menu_description, menu_image, menu_sku, restoran_id, restoran_name, restoran_logo;
    BigDecimal menu_price;

    public Makanan(String menu_id, String menu_name, String menu_description, String menu_image, String menu_sku, String restoran_id, String restoran_name, String restoran_logo, BigDecimal menu_price) {
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.menu_description = menu_description;
        this.menu_image = menu_image;
        this.menu_sku = menu_sku;
        this.restoran_id = restoran_id;
        this.restoran_name = restoran_name;
        this.restoran_logo = restoran_logo;
        this.menu_price = menu_price;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_description() {
        return menu_description;
    }

    public void setMenu_description(String menu_description) {
        this.menu_description = menu_description;
    }

    public String getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(String menu_image) {
        this.menu_image = menu_image;
    }

    public String getMenu_sku() {
        return menu_sku;
    }

    public void setMenu_sku(String menu_sku) {
        this.menu_sku = menu_sku;
    }

    public String getRestoran_id() {
        return restoran_id;
    }

    public void setRestoran_id(String restoran_id) {
        this.restoran_id = restoran_id;
    }

    public String getRestoran_name() {
        return restoran_name;
    }

    public void setRestoran_name(String restoran_name) {
        this.restoran_name = restoran_name;
    }

    public String getRestoran_logo() {
        return restoran_logo;
    }

    public void setRestoran_logo(String restoran_logo) {
        this.restoran_logo = restoran_logo;
    }

    public BigDecimal getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(BigDecimal menu_price) {
        this.menu_price = menu_price;
    }
}
