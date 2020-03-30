package com.ecommerce.shop.Shopflix.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name ="cart")
public class CartProducts {
    @NotNull
    private String user_id;
    @Id
    private String product_id;

    public CartProducts(String user_id, @NotNull String product_id) {
        this.user_id = user_id;
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public CartProducts() {

    }
}
