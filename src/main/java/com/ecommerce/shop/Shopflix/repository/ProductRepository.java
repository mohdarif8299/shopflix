package com.ecommerce.shop.Shopflix.repository;

import com.ecommerce.shop.Shopflix.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    @Query(value = "SELECT p FROM Product p where p.product_brand = ?1")
    List<Product> findByProductBrand (String product_brand);
}
