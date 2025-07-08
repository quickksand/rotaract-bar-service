package com.lichius.rac.ansbach.altstadtfest.controller;

import com.lichius.rac.ansbach.altstadtfest.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Greet a user", description = "This endpoint greets the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful greeting"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/hi")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

    @Operation(summary = "Get all orders", description = "This endpoint gets all currently saved orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getOrders() {
        return new ResponseEntity<List<PurchaseOrder>>(purchaseOrderService.findOrders(), HttpStatus.OK);
    }

    @Operation(summary = "Get all ordered items by item name", description = "This endpoint gets all currently saved ordereditems by item name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid name provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(params = "itemName")
    public ResponseEntity<List<OrderedItem>> getOrdersByItemName(@RequestParam String itemName) {
        return new ResponseEntity<List<OrderedItem>>(purchaseOrderService.findOrdersByItemName(itemName), HttpStatus.OK);
    }

    @Operation(summary = "Get Order by id", description = "This endpoint gets all orders by provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid id provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PurchaseOrder> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<PurchaseOrder>(purchaseOrderService.findOrderById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create new Order", description = "This endpoint creates a new purchase order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid details provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody PurchaseOrder orderDTO) {
        PurchaseOrder saved = purchaseOrderService.createPurchaseOrder(orderDTO);
        return new ResponseEntity<PurchaseOrder>(saved, HttpStatus.OK);
    }

}
