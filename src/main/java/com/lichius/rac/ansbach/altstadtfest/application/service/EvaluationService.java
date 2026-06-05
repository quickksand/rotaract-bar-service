package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.OrderedItem;
import com.lichius.rac.ansbach.altstadtfest.application.model.PaymentMethod;
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

        double cashRevenue = 0.0;
        double paypalRevenue = 0.0;
        double cashTips = 0.0;
        double paypalTips = 0.0;
        int totalCupsIssued = 0;
        int totalCupsReturned = 0;

        for (PurchaseOrder order : orders) {
            // Brutto-Umsatz des Auftrags berechnen (customPrice bei Flaschenverkäufen berücksichtigen)
            double orderGrossRevenue = 0.0;
            for (OrderedItem item : order.getItems()) {
                if (item.getProduct() != null) {
                    if (item.isBottleSale() && item.getCustomPrice() != null) {
                        orderGrossRevenue += item.getCustomPrice();
                    } else {
                        orderGrossRevenue += item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                                .doubleValue();
                    }
                }
            }

            // freeDrinkDiscount abziehen → Netto-Umsatz
            double discount = order.getFreeDrinkDiscount() != null ? order.getFreeDrinkDiscount() : 0.0;
            double orderNetRevenue = orderGrossRevenue - discount;

            double tipAmount = order.getTipAmount() != null ? order.getTipAmount() : 0.0;

            // Aufspaltung nach Zahlungsmethode (null = CASH als Legacy-Fallback)
            boolean isCash = order.getPaymentMethod() == null
                    || order.getPaymentMethod() == PaymentMethod.CASH;
            if (isCash) {
                cashRevenue += orderNetRevenue;
                cashTips += tipAmount;
            } else {
                paypalRevenue += orderNetRevenue;
                paypalTips += tipAmount;
            }

            if (order.getTokensIssued() != null) {
                totalCupsIssued += order.getTokensIssued();
            }
            if (order.getReturnedCupsCount() != null) {
                totalCupsReturned += order.getReturnedCupsCount();
            }
        }

        double totalRevenue = cashRevenue + paypalRevenue;
        double totalTips = cashTips + paypalTips;
        int cupsOutstanding = totalCupsIssued - totalCupsReturned;
        double expectedCash = cashRevenue + cashTips + (cupsOutstanding * depositAmountPerCup);

        EvaluationSummaryDto dto = new EvaluationSummaryDto();
        dto.setTotalRevenue(totalRevenue);
        dto.setTotalTips(totalTips);
        dto.setCashRevenue(cashRevenue);
        dto.setPaypalRevenue(paypalRevenue);
        dto.setCashTips(cashTips);
        dto.setPaypalTips(paypalTips);
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
