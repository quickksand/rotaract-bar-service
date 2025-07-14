package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.application.service.PurchaseOrderService;
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
//@CrossOrigin(origins = "http://localhost:4200")
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

    @PostMapping
    public ResponseEntity<PurchaseOrderDto> createOrder(@RequestBody PurchaseOrderDto purchaseOrderDto) {
        PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
        return new ResponseEntity<PurchaseOrderDto>(purchaseOrderMapper.toDto(purchaseOrder), HttpStatus.OK);
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
        Optional<PurchaseOrder> purchaseOrder = Optional.ofNullable(purchaseOrderService.findOrderById(id));
        if (purchaseOrder.isPresent()) {
            return new ResponseEntity<>(
                    purchaseOrderMapper.toDto(purchaseOrder.get()),
                    HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hi")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }


}
