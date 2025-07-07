package com.lichius.rac.ansbach.altstadtfest.repository;

import com.lichius.rac.ansbach.altstadtfest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
}
