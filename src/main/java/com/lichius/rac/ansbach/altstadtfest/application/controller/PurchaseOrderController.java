package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.application.service.PurchaseOrderService;
import com.lichius.rac.ansbach.altstadtfest.exception.NotFoundException;
import com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper.PurchaseOrderMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rotaract.bar.infrastructure.api.controller.PurchaseOrderControllerApi;
import rotaract.bar.infrastructure.api.controller.model.PurchaseOrderDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class PurchaseOrderController implements PurchaseOrderControllerApi {

    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderController(
            PurchaseOrderService purchaseOrderService,
            PurchaseOrderMapper purchaseOrderMapper
    ) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<PurchaseOrderDto> createOrder(@RequestBody PurchaseOrderDto purchaseOrderDto) {
        PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
        return new ResponseEntity<>(purchaseOrderMapper.toDto(purchaseOrder), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderDto>> getOrders() {
        return new ResponseEntity<>(
                purchaseOrderService.findOrders()
                        .stream()
                        .map(purchaseOrderMapper::toDto)
                        .collect(Collectors.toList())
                , HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PurchaseOrderDto> getOrderById(@PathVariable Long id) {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.findOrderById(id);
        return purchaseOrder
                .map(order -> new ResponseEntity<>(
                    purchaseOrderMapper.toDto(order),
                    HttpStatus.OK))
                .orElseThrow(NotFoundException::new);
    }
}
