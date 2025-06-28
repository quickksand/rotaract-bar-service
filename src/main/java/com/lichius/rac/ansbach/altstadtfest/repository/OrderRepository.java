package com.lichius.rac.ansbach.altstadtfest.repository;

import com.lichius.rac.ansbach.altstadtfest.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
