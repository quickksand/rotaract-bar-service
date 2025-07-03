package com.lichius.rac.ansbach.altstadtfest.controller;

import com.lichius.rac.ansbach.altstadtfest.model.Order;
import com.lichius.rac.ansbach.altstadtfest.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
//@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return new ResponseEntity<List<Order>>(orderService.findOrders(), HttpStatus.OK);
    }

    // /api/orders/hi
    @GetMapping("/hi")
    public ResponseEntity<String> hello(){
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<Order>(orderService.findOrderById(id), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        Order saved = orderService.createOrder(order);
        return new ResponseEntity<Order>(saved, HttpStatus.OK);
    }

}
