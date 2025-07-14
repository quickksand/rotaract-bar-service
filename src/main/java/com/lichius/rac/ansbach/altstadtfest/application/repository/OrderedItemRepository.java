package com.lichius.rac.ansbach.altstadtfest.application.repository;

import com.lichius.rac.ansbach.altstadtfest.application.model.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {

    OrderedItem findByPurchaseOrder_Id(Long purchaseOrderId);

    List<OrderedItem> findByProduct_Name(String productName);
}
