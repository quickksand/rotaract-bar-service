package com.lichius.rac.ansbach.altstadtfest.service;

import com.lichius.rac.ansbach.altstadtfest.model.Order;
import com.lichius.rac.ansbach.altstadtfest.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    // LOGIK HIER

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order){
        order.setOrderedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

}
