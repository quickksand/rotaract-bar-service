package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.model.ProductsMeta;
import com.lichius.rac.ansbach.altstadtfest.application.service.ProductService;
import com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rotaract.bar.infrastructure.api.controller.ProductControllerApi;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;
import rotaract.bar.infrastructure.api.controller.model.ProductsMetaDto;
import rotaract.bar.infrastructure.api.controller.model.UpdateProductRequestDto;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RestController
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

    @Override
    public ResponseEntity<List<ProductDto>> getProducts() {
        return new ResponseEntity<>(
                productService.getProducts()
                        .stream()
                        .map(productMapper::toDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(Long id, UpdateProductRequestDto request) {
        Product updated = productService.updateProduct(id, request.getPrice(), request.getOutOfStock());
        return ResponseEntity.ok(productMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<List<ProductDto>> resetProducts() {
        List<ProductDto> products = productService.resetProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<ProductsMetaDto> getProductsMeta() {
        ProductsMeta meta = productService.getProductsMeta();
        ProductsMetaDto dto = new ProductsMetaDto();
        dto.setProductsUpdatedAt(meta.getProductsUpdatedAt().atOffset(ZoneOffset.UTC));
        return ResponseEntity.ok(dto);
    }
}
