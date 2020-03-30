package com.ecommerce.shop.Shopflix.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private String product_id;
    private String product_name,product_description,product_image,product_brand,product_price,product_status;

    @Override
    public String toString() {
        return "Product{" +
                "product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_description='" + product_description + '\'' +
                ", product_image='" + product_image + '\'' +
                ", product_brand='" + product_brand + '\'' +
                ", product_price='" + product_price + '\'' +
                ", product_status='" + product_status + '\'' +
                '}';
    }

    public Product(String product_id, String product_name, String product_description, String product_image, String product_brand, String product_price, String product_status) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_image = product_image;
        this.product_brand = product_brand;
        this.product_price = product_price;
        this.product_status = product_status;
    }

    public Product() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }
}
