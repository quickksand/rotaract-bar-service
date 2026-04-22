package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.application.repository.OrderedItemRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import rotaract.bar.infrastructure.api.controller.model.PurchaseOrderDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
//@Transactional
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderedItemRepository orderedItemRepository;
    private final ProductRepository productRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, OrderedItemRepository orderedItemRepository, ProductRepository productRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.productRepository = productRepository;
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrderDto purchaseOrderDTO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setReturnedCupsCount(purchaseOrderDTO.getReturnedCupsCount());

        if (purchaseOrderDTO.getTipAmount() != null) {
            purchaseOrder.setTipAmount(java.math.BigDecimal.valueOf(purchaseOrderDTO.getTipAmount()));
        }

        purchaseOrderDTO.getItems().forEach(itemDTO -> {
                    OrderedItem item = new OrderedItem();

            if (itemDTO.getProductId() != null) {
                Optional<Product> itemProduct = productRepository.findById(itemDTO.getProductId());

                if (itemProduct.isEmpty()) {
                    System.out.println("WARNUNG: Product nicht gefunden: " + itemDTO.getProductId().byteValue());
                    return; // Item überspringen
                }

                item.setProduct(itemProduct.get());

                Optional<Integer> optionalQuantity = Optional.ofNullable(itemDTO.getQuantity());
                item.setQuantity(optionalQuantity.orElse(0));
                purchaseOrder.addOrderedItem(item);
            }

        });

        // tokensIssued = Summe der Mengen aller Artikel mit Pfand
        int tokensIssued = purchaseOrder.getItems().stream()
                .filter(item -> item.getProduct() != null && item.getProduct().isRequiresDeposit())
                .mapToInt(OrderedItem::getQuantity)
                .sum();
        purchaseOrder.setTokensIssued(tokensIssued);

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder findOrderById(Long id) {
        return purchaseOrderRepository.findOrderById(id);
    }

    public List<PurchaseOrder> findOrders(Integer year, OffsetDateTime from, OffsetDateTime to) {
        if (from != null && to != null) {
            return purchaseOrderRepository.findByOrderedAtBetween(from.toLocalDateTime(), to.toLocalDateTime());
        }
        if (year != null) {
            LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
            LocalDateTime end = start.plusYears(1).minusNanos(1);
            return purchaseOrderRepository.findByOrderedAtBetween(start, end);
        }
        return purchaseOrderRepository.findAll();
    }

    public List<OrderedItem> findOrdersByItemName(String name) {
        return orderedItemRepository.findByProduct_Name(name);
    }


}
