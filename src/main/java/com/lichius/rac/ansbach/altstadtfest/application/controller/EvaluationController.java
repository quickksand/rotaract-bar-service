package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.service.EvaluationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rotaract.bar.infrastructure.api.controller.EvaluationControllerApi;
import rotaract.bar.infrastructure.api.controller.model.EvaluationSummaryDto;
import rotaract.bar.infrastructure.api.controller.model.ProductSalesEntryDto;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
public class EvaluationController implements EvaluationControllerApi {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Override
    public ResponseEntity<EvaluationSummaryDto> getEvaluationSummary(
            Integer year, OffsetDateTime from, OffsetDateTime to,
            Integer startCups, Double depositAmountPerCup) {
        int cups = startCups != null ? startCups : 0;
        double deposit = depositAmountPerCup != null ? depositAmountPerCup : 1.0;
        return new ResponseEntity<>(
                evaluationService.getEvaluationSummary(year, from, to, cups, deposit),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ProductSalesEntryDto>> getTopProducts(
            Integer year, OffsetDateTime from, OffsetDateTime to,
            Integer limit, String sortOrder) {
        int effectiveLimit = limit != null ? limit : 10;
        String effectiveOrder = sortOrder != null ? sortOrder : "desc";
        return new ResponseEntity<>(
                evaluationService.getTopProducts(year, from, to, effectiveLimit, effectiveOrder),
                HttpStatus.OK);
    }
}
