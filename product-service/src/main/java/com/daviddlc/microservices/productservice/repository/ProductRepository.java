package com.daviddlc.microservices.productservice.repository;

import com.daviddlc.microservices.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
