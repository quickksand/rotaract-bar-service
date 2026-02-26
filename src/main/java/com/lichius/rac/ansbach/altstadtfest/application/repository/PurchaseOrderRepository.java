package com.lichius.rac.ansbach.altstadtfest.application.repository;

import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findOrderById(Long id);

}
