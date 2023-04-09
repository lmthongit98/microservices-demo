package com.tma.orderservice.service;

import com.tma.orderservice.dto.OrderRequest;
import com.tma.orderservice.dto.UserDto;

public interface OrderService {

    void placeOrder(OrderRequest orderRequest, UserDto userDto);

}
