package com.tma.orderservice.service.impl;

import com.tma.orderservice.client.InventoryClient;
import com.tma.orderservice.dto.InventoryResponse;
import com.tma.orderservice.dto.OrderLineItemsDto;
import com.tma.orderservice.dto.OrderRequest;
import com.tma.orderservice.model.Order;
import com.tma.orderservice.model.OrderLineItems;
import com.tma.orderservice.repository.OrderRepository;
import com.tma.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        log.info("Starting order for order request {}", orderRequest);
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToEntity).toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        // Call inventory service, and place order if product is in stock
        List<InventoryResponse> inventoryResponses;
        try {
            inventoryResponses = inventoryClient.isInStock(skuCodes);
        } catch (Exception e) {
            log.error("Getting exception while calling inventory api", e);
            throw e;
        }
        log.info("Inventory check is: {}", inventoryResponses);
        boolean allProductsInStock = inventoryResponses.stream().allMatch(InventoryResponse::isInStock);
        if (skuCodes.size() != inventoryResponses.size() || !allProductsInStock) {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
        orderRepository.save(order);
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) {
        return modelMapper.map(orderLineItemsDto, OrderLineItems.class);
    }

    private OrderLineItemsDto mapToDto(OrderLineItems orderLineItems) {
        return modelMapper.map(orderLineItems, OrderLineItemsDto.class);
    }

}
