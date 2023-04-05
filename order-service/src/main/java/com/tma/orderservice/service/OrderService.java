package com.tma.orderservice.service;

import com.tma.orderservice.dto.OrderRequest;
import com.tma.orderservice.model.Order;

public interface OrderService {

    void placeOrder(OrderRequest orderRequest);

}
