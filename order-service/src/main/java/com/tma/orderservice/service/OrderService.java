package com.tma.orderservice.service;

import com.tma.common.dto.user.UserDto;
import com.tma.orderservice.dto.OrderRequest;

public interface OrderService {

    void placeOrder(OrderRequest orderRequest, UserDto userDto);

}
