package com.ecommerce.shop.Shopflix.repository;

import com.ecommerce.shop.Shopflix.entity.CartProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartProducts,String> {

    @Query(value = "SELECT C.product_id  FROM CartProducts C  WHERE C.user_id = ?1")
    List<String> cartProducts(String user_id);
}
