package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.application.repository.OrderedItemRepository;
import com.lichius.rac.ansbach.altstadtfest.application.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import rotaract.bar.infrastructure.api.controller.model.EvaluationSummaryDto;
import rotaract.bar.infrastructure.api.controller.model.ProductSalesEntryDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EvaluationService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderedItemRepository orderedItemRepository;

    public EvaluationService(PurchaseOrderRepository purchaseOrderRepository,
                             OrderedItemRepository orderedItemRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderedItemRepository = orderedItemRepository;
    }

    public EvaluationSummaryDto getEvaluationSummary(Integer year, OffsetDateTime from, OffsetDateTime to,
                                                     int startCups, double depositAmountPerCup) {
        LocalDateTime[] range = resolveRange(year, from, to);
        LocalDateTime effectiveFrom = range[0];
        LocalDateTime effectiveTo = range[1];

        List<PurchaseOrder> orders = effectiveFrom != null
                ? purchaseOrderRepository.findByOrderedAtBetween(effectiveFrom, effectiveTo)
                : purchaseOrderRepository.findAll();

        double totalRevenue = 0.0;
        double totalTips = 0.0;
        int totalCupsIssued = 0;
        int totalCupsReturned = 0;

        for (PurchaseOrder order : orders) {
            for (var item : order.getItems()) {
                if (item.getProduct() != null) {
                    totalRevenue += item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
                            .doubleValue();
                }
            }
            if (order.getTipAmount() != null) {
                totalTips += order.getTipAmount().doubleValue();
            }
            if (order.getTokensIssued() != null) {
                totalCupsIssued += order.getTokensIssued();
            }
            if (order.getReturnedCupsCount() != null) {
                totalCupsReturned += order.getReturnedCupsCount();
            }
        }

        int cupsOutstanding = totalCupsIssued - totalCupsReturned;
        double netDepositCash = cupsOutstanding * depositAmountPerCup;
        double expectedCash = totalRevenue + totalTips + netDepositCash;

        EvaluationSummaryDto dto = new EvaluationSummaryDto();
        dto.setTotalRevenue(totalRevenue);
        dto.setTotalTips(totalTips);
        dto.setExpectedCashInRegister(expectedCash);
        dto.setDepositAmountPerCup(depositAmountPerCup);
        dto.setTotalCupsIssued(totalCupsIssued);
        dto.setTotalCupsReturned(totalCupsReturned);
        dto.setCupsOutstanding(cupsOutstanding);
        dto.setStartCups(startCups);
        dto.setOrderCount(orders.size());
        return dto;
    }

    public List<ProductSalesEntryDto> getTopProducts(Integer year, OffsetDateTime from, OffsetDateTime to,
                                                     int limit, String sortOrder) {
        LocalDateTime[] range = resolveRange(year, from, to);
        List<Object[]> rows = range[0] != null
                ? orderedItemRepository.aggregateProductSalesInRange(range[0], range[1])
                : orderedItemRepository.aggregateProductSalesAll();

        List<ProductSalesEntryDto> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            ProductSalesEntryDto entry = new ProductSalesEntryDto();
            entry.setProductId((Long) row[0]);
            entry.setProductName((String) row[1]);
            entry.setCategory(mapCategory(row[2]));
            entry.setTotalQuantitySold(((Number) row[3]).intValue());
            entry.setTotalRevenue(((Number) row[4]).doubleValue());
            entry.setRank(i + 1);
            result.add(entry);
        }

        // desc = best sellers first (default), asc = worst sellers first
        if ("asc".equalsIgnoreCase(sortOrder)) {
            Collections.reverse(result);
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setRank(i + 1);
            }
        }

        return result.stream().limit(limit).toList();
    }

    private LocalDateTime[] resolveRange(Integer year, OffsetDateTime from, OffsetDateTime to) {
        if (from != null && to != null) {
            return new LocalDateTime[]{from.toLocalDateTime(), to.toLocalDateTime()};
        }
        if (year != null) {
            LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
            return new LocalDateTime[]{start, start.plusYears(1).minusNanos(1)};
        }
        return new LocalDateTime[]{null, null};
    }

    private ProductSalesEntryDto.CategoryEnum mapCategory(Object categoryValue) {
        if (categoryValue == null) return null;
        String name = categoryValue.toString();
        try {
            return ProductSalesEntryDto.CategoryEnum.fromValue(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
