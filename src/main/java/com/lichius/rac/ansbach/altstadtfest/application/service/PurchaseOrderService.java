package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.controller.advice.InvalidOrderException;
import com.lichius.rac.ansbach.altstadtfest.application.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.application.model.PaymentMethod;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.PurchaseOrderRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rotaract.bar.infrastructure.api.controller.model.BatchOrderImportDto;
import rotaract.bar.infrastructure.api.controller.model.OrderedItemDto;
import rotaract.bar.infrastructure.api.controller.model.PurchaseOrderDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;

    public PurchaseOrder createPurchaseOrder(PurchaseOrderDto purchaseOrderDTO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setReturnedCupsCount(purchaseOrderDTO.getReturnedCupsCount());
        purchaseOrder.setTipAmount(purchaseOrderDTO.getTipAmount());
        purchaseOrder.setFreeDrinksEarned(purchaseOrderDTO.getFreeDrinksEarned());
        purchaseOrder.setFreeDrinkDiscount(purchaseOrderDTO.getFreeDrinkDiscount());
        if (purchaseOrderDTO.getPaymentMethod() != null) {
            purchaseOrder.setPaymentMethod(PaymentMethod.valueOf(purchaseOrderDTO.getPaymentMethod().getValue()));
        }

        List<@Valid OrderedItemDto> orderDTOItems = purchaseOrderDTO.getItems();
        if (orderDTOItems.isEmpty()) {
            throw new InvalidOrderException("Order contains no items");
        }

        orderDTOItems.forEach(itemDTO -> {
            OrderedItem item = new OrderedItem();

            if (itemDTO.getProductId() != null) {
                Product itemProduct = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new InvalidOrderException(
                                "Product not found: " + itemDTO.getProductId()
                        ));

                item.setProduct(itemProduct);
                item.setQuantity(Optional.ofNullable(itemDTO.getQuantity()).orElse(0));
                item.setBottleSale(Boolean.TRUE.equals(itemDTO.getBottleSale()));
                item.setCustomPrice(itemDTO.getCustomPrice());
                purchaseOrder.addOrderedItem(item);
            } else {
                throw new InvalidOrderException("Product ID is Empty");
            }
        });

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public Optional<PurchaseOrder> findOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    public List<PurchaseOrder> findOrders() {
        return purchaseOrderRepository.findAll();
    }

    public BatchImportResult importOrdersBatch(BatchOrderImportDto batchOrderImportDto) {
        List<PurchaseOrder> created = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        List<PurchaseOrderDto> orders = Objects.requireNonNullElse(batchOrderImportDto.getOrders(), List.of());
        for (int i = 0; i < orders.size(); i++) {
            try {
                PurchaseOrder order = createPurchaseOrder(orders.get(i));
                created.add(order);
            } catch (Exception e) {
                log.error("Fehler beim Importieren von Bestellung an Index {}: {}", i, e.getMessage());
                errors.add("Order at index " + i + ": " + e.getMessage());
            }
        }

        return new BatchImportResult(created, errors);
    }

    public record BatchImportResult(List<PurchaseOrder> created, List<String> errors) {
    }

}
