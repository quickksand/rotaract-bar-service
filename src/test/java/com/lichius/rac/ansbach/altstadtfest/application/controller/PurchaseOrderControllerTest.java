package com.lichius.rac.ansbach.altstadtfest.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lichius.rac.ansbach.altstadtfest.application.controller.PurchaseOrderController;
import com.lichius.rac.ansbach.altstadtfest.application.model.PurchaseOrder;
import com.lichius.rac.ansbach.altstadtfest.application.service.PurchaseOrderService;
import com.lichius.rac.ansbach.altstadtfest.exception.NotFoundException;
import com.lichius.rac.ansbach.altstadtfest.infrastructure.mapper.PurchaseOrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rotaract.bar.infrastructure.api.controller.model.OrderedItemDto;
import rotaract.bar.infrastructure.api.controller.model.PurchaseOrderDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseOrderController.class)
public class PurchaseOrderControllerTest {

    @MockitoBean
    PurchaseOrderService purchaseOrderService;

    @MockitoBean
    PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetPurchaseOrders() throws Exception {
        mockMvc.perform(get("/api/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    void testGetPurchaseOrderById() throws Exception {
        PurchaseOrder purchaseOrder = PurchaseOrder.builder().id(1L).build();
        PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
        purchaseOrderDto.setId(purchaseOrder.getId());

        given(purchaseOrderService.findOrderById(1L)).willReturn(Optional.of(purchaseOrder));
        given(purchaseOrderMapper.toDto(any())).willReturn(purchaseOrderDto);

        mockMvc.perform(get("/api/orders/" + purchaseOrder.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(purchaseOrderDto.getId().intValue())));
    }

    @Test
    void testGetPurchaseOrderThrowsException() throws Exception {
//        given(purchaseOrderService.findOrderById(any())).willThrow(NotFoundException.class);
        mockMvc.perform(get("/api/orders/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostOrder() throws Exception {
        PurchaseOrder purchaseOrder = PurchaseOrder.builder().id(1L).build();
        PurchaseOrderDto someOrderDto = new PurchaseOrderDto();
        someOrderDto.setId(1L);

        given(purchaseOrderService.createPurchaseOrder(any())).willReturn(purchaseOrder);
        given(purchaseOrderMapper.toDto(purchaseOrder)).willReturn(someOrderDto);

        mockMvc.perform(post("/api/orders")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(someOrderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));

        verify(purchaseOrderService).createPurchaseOrder(someOrderDto);
        verify(purchaseOrderMapper).toDto(purchaseOrder);
    }
}
