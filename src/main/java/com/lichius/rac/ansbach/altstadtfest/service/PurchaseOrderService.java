package com.lichius.rac.ansbach.altstadtfest.service;

import com.lichius.rac.ansbach.altstadtfest.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.repository.OrderedItemRepository;
import com.lichius.rac.ansbach.altstadtfest.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Transactional
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderedItemRepository orderedItemRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, OrderedItemRepository orderedItemRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderedItemRepository = orderedItemRepository;
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
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
