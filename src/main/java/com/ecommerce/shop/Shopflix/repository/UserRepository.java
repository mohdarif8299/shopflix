package com.ecommerce.shop.Shopflix.repository;

import com.ecommerce.shop.Shopflix.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,String> {
}
