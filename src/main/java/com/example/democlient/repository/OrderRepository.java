package com.example.democlient.repository;

import com.example.democlient.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClient_IdClient(Long idClient);

}
