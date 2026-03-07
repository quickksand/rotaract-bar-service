package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rotaract.bar.infrastructure.api.controller.model.OrderedItemDto;
import rotaract.bar.infrastructure.api.controller.model.PurchaseOrderDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
//@Transactional
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, OrderedItemRepository orderedItemRepository, ProductRepository productRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.productRepository = productRepository;
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrderDto purchaseOrderDTO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setReturnedCupsCount(purchaseOrderDTO.getReturnedCupsCount());

        if (orderDTOItems.isEmpty()) {
            return null;
        }

        orderDTOItems.forEach(itemDTO -> {
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

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public Optional<PurchaseOrder> findOrderById(Long id) {
        return purchaseOrderRepository.findOrderById(id);
    }

    public List<PurchaseOrder> findOrders() {
        return purchaseOrderRepository.findAll();
    }

}
