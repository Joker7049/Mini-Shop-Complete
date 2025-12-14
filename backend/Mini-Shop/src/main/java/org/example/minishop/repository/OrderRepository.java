package org.example.minishop.repository;

import org.example.minishop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order,Long> {

}
