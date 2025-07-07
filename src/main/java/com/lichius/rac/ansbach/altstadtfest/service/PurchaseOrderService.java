package com.lichius.rac.ansbach.altstadtfest.service;

import com.lichius.rac.ansbach.altstadtfest.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.model.Product;
import com.lichius.rac.ansbach.altstadtfest.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.repository.OrderedItemRepository;
import com.lichius.rac.ansbach.altstadtfest.repository.ProductRepository;
import com.lichius.rac.ansbach.altstadtfest.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrderDTO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrderDTO.getItems().forEach(itemDTO -> {
                    OrderedItem item = new OrderedItem();

                    List<Product> itemProducts = productRepository.findByName(itemDTO.getProduct().getName());

                    if (itemProducts.isEmpty()) {
                        System.out.println("WARNUNG: Product nicht gefunden: " + itemDTO.getProduct().getName());
                        return; // Item überspringen
                    }

                    item.setProduct(itemProducts.getFirst());
                    item.setQuantity(itemDTO.getQuantity());
                    purchaseOrder.addOrderedItem(item);
                }
        );

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder findOrderById(Long id) {
        return purchaseOrderRepository.findOrderById(id);
    }

    public List<PurchaseOrder> findOrders() {
        return purchaseOrderRepository.findAll();
    }

    public List<OrderedItem> findOrdersByItemName(String name) {
        return orderedItemRepository.findByProduct_Name(name);
    }


}
