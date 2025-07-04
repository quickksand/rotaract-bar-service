package com.lichius.rac.ansbach.altstadtfest.controller;

import com.lichius.rac.ansbach.altstadtfest.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.service.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
//@CrossOrigin(origins = "http://localhost:4200")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getOrders() {
        return new ResponseEntity<List<PurchaseOrder>>(purchaseOrderService.findOrders(), HttpStatus.OK);
    }

    @GetMapping(params = "itemName")
    public ResponseEntity<List<OrderedItem>> getOrdersByItemName(@RequestParam String itemName) {
        return new ResponseEntity<List<OrderedItem>>(purchaseOrderService.findOrdersByItemName(itemName), HttpStatus.OK);
    }

    // /api/orders/hi
    @GetMapping("/hi")
    public ResponseEntity<String> hello(){
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PurchaseOrder> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<PurchaseOrder>(purchaseOrderService.findOrderById(id), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody PurchaseOrder order) {
        PurchaseOrder saved = purchaseOrderService.createPurchaseOrder(order);
        return new ResponseEntity<PurchaseOrder>(saved, HttpStatus.OK);
    }

}
