package com.lichius.rac.ansbach.altstadtfest.repository;

import com.lichius.rac.ansbach.altstadtfest.model.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {
    List<OrderedItem> findByOrderId(Long orderId);

    List<OrderedItem> findByProductId(Long productId);

}
