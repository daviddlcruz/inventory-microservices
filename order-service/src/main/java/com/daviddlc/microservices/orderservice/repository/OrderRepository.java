package com.daviddlc.microservices.orderservice.repository;

import com.daviddlc.microservices.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
