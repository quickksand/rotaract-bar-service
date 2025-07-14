package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.service.ProductService;
import com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rotaract.bar.infrastructure.api.controller.ProductControllerApi;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController implements ProductControllerApi {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(
            ProductService productService,
            ProductMapper productMapper
    ) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        return new ResponseEntity<>(
                productService.getProducts()
                        .stream()
                        .map(productMapper::toDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
//        return new ResponseEntity<List<ProductDto>>(List.of(new ProductDto()), HttpStatus.OK);
    }
}
