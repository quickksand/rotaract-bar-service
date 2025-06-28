package com.lichius.rac.ansbach.altstadtfest.controller;

import com.lichius.rac.ansbach.altstadtfest.model.Order;
import com.lichius.rac.ansbach.altstadtfest.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
//@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<String> hello(){
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        Order saved = orderService.createOrder(order);
        return new ResponseEntity<Order>(saved, HttpStatus.OK);
    }

}
