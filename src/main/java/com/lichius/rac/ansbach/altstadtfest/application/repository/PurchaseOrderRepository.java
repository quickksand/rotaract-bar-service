package com.lichius.rac.ansbach.altstadtfest.application.repository;

import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    PurchaseOrder findOrderById(Long id);

    List<PurchaseOrder> findByOrderedAtBetween(LocalDateTime from, LocalDateTime to);

}
