package com.lichius.rac.ansbach.altstadtfest.repository;

import com.lichius.rac.ansbach.altstadtfest.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    PurchaseOrder findOrderById(Long id);

}
