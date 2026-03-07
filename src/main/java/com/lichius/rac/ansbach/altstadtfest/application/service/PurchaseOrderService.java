package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.PurchaseOrderRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rotaract.bar.infrastructure.api.controller.model.OrderedItemDto;
import rotaract.bar.infrastructure.api.controller.model.PurchaseOrderDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;

    public PurchaseOrder createPurchaseOrder(PurchaseOrderDto purchaseOrderDTO) {
        PurchaseOrder purchaseOrder = PurchaseOrder.builder().returnedCupsCount(purchaseOrderDTO.getReturnedCupsCount()).build();

        List<@Valid OrderedItemDto> orderDTOItems = purchaseOrderDTO.getItems();
        if (orderDTOItems.isEmpty()) {
            return null;
        }

        orderDTOItems.forEach(itemDTO -> {
            OrderedItem item = new OrderedItem();

            if (itemDTO.getProductId() != null) {
                Optional<Product> itemProduct = productRepository.findById(itemDTO.getProductId());
                if (itemProduct.isEmpty()) {
                    log.error("WARNUNG: Product nicht gefunden: " + itemDTO.getProductId().byteValue());
                    return; // Item überspringen
                }

                item.setProduct(itemProduct.get());

                Optional<Integer> optionalQuantity = Optional.ofNullable(itemDTO.getQuantity());
                item.setQuantity(optionalQuantity.orElse(0));
                purchaseOrder.addOrderedItem(item);
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

}
