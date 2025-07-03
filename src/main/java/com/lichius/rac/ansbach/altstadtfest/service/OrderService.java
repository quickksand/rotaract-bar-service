package com.lichius.rac.ansbach.altstadtfest.service;

import com.lichius.rac.ansbach.altstadtfest.model.Order;
import com.lichius.rac.ansbach.altstadtfest.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
//    private final OrderedItemRepository orderedItemRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
//        this.orderedItemRepository = orderedItemRepository;
    }

    public Order createOrder(Order order){
        System.out.println("Received items: " + order.getItems());
        return orderRepository.save(order);
    }

    public Order findOrderById(Long id) {
        return orderRepository.findOrderById(id);
    }

    public List<Order> findOrders() {
        return orderRepository.findAll();
    }

}
