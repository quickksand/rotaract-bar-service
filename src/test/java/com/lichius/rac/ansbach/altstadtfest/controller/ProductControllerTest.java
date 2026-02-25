package com.lichius.rac.ansbach.altstadtfest.controller;

import com.lichius.rac.ansbach.altstadtfest.application.controller.ProductController;
import com.lichius.rac.ansbach.altstadtfest.application.model.Product;
import com.lichius.rac.ansbach.altstadtfest.application.service.ProductService;
import com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rotaract.bar.infrastructure.api.controller.model.ProductDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @MockitoBean
    ProductMapper productMapper;

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(
                // DRINKS
                new Product("Cuba Libre", new BigDecimal("6.50"), ProductDto.CategoryEnum.DRINKS),
                new Product("Gin Tonic", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS),
                new Product("Moscow Mule", new BigDecimal("7.00"), ProductDto.CategoryEnum.DRINKS)
        );
        List<ProductDto> productDtos = Arrays.asList(
                new ProductDto().name("Cuba Libre").price(6.50).category(ProductDto.CategoryEnum.DRINKS),
                new ProductDto().name("Gin Tonic").price(7.00).category(ProductDto.CategoryEnum.DRINKS),
                new ProductDto().name("Moscow Mule").price(7.00).category(ProductDto.CategoryEnum.DRINKS)
        );

        given(productService.getProducts()).willReturn(products);
        given(productMapper.toDto(products.get(0))).willReturn(productDtos.get(0));
        given(productMapper.toDto(products.get(1))).willReturn(productDtos.get(1));
        given(productMapper.toDto(products.get(2))).willReturn(productDtos.get(2));

        mockMvc.perform(get("/api/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].name", is("Cuba Libre")))
                .andExpect(jsonPath("$[1].name", is("Gin Tonic")))
                .andExpect(jsonPath("$[2].name", is("Moscow Mule")));
    }
}
