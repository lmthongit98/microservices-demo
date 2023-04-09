package com.tma.orderservice.service.impl;

import com.tma.common.dto.email.EmailId;
import com.tma.common.dto.inventory.InventoryResponse;
import com.tma.common.dto.user.UserDto;
import com.tma.common.service.EmailService;
import com.tma.orderservice.client.InventoryServiceClient;
import com.tma.orderservice.constant.OrderConstant;
import com.tma.orderservice.dto.OrderLineItemsDto;
import com.tma.orderservice.dto.OrderRequest;
import com.tma.orderservice.dto.OrderEmailTemplate;
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
    private final InventoryServiceClient inventoryServiceClient;
    private final EmailService emailService;

    @Override
    public void placeOrder(OrderRequest orderRequest, UserDto userDto) {
        log.info("Starting order for order request {}", orderRequest);
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToEntity).toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        // Call inventory service, and place order if product is in stock
        List<InventoryResponse> inventoryResponses;
        try {
            inventoryResponses = inventoryServiceClient.isInStock(skuCodes);
        } catch (Exception e) {
            log.error("Getting exception while calling inventory api", e);
            throw e;
        }
        log.info("Inventory check is: {}", inventoryResponses);
        boolean allProductsInStock = inventoryResponses.stream().allMatch(InventoryResponse::isInStock);
        if (skuCodes.size() != inventoryResponses.size() || !allProductsInStock) {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
        Order savedOrder = orderRepository.save(order);

        // send notification to user
        OrderEmailTemplate template = new OrderEmailTemplate(OrderConstant.ORDER_EMAIL_TEMPLATE, savedOrder);
        EmailId toEmail = new EmailId(userDto.getName(), userDto.getEmail());
        EmailId fromEmail =  new EmailId("", OrderConstant.NO_REPLY);
        String subject = OrderConstant.EMAIL_SUBJECT.replace("{}", savedOrder.getOrderNumber());
        emailService.sendMail(template, toEmail, fromEmail, subject);
        log.info("Email sent.");
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) {
        return modelMapper.map(orderLineItemsDto, OrderLineItems.class);
    }

    private OrderLineItemsDto mapToDto(OrderLineItems orderLineItems) {
        return modelMapper.map(orderLineItems, OrderLineItemsDto.class);
    }

}
