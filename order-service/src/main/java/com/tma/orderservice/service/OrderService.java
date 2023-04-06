package com.tma.orderservice.service;

import com.tma.orderservice.dto.OrderRequest;

public interface OrderService {

    void placeOrder(OrderRequest orderRequest);

}
