package com.lichius.rac.ansbach.altstadtfest.application.service;

import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    private List<Product> getDefaultProducts() {
        return Arrays.asList(
                new Product("Bier", new BigDecimal("3.00")),
                new Product("Cuba Libre", new BigDecimal("5.50")),
                new Product("Cola", new BigDecimal("3.00"))
        );
    }

    @PostConstruct
    public void initalizeProducts() {
        if (productRepository.count() == 0) {
            productRepository.saveAll(getDefaultProducts());
        }
    }
}
