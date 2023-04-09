package com.tma.orderservice.controller;

import com.tma.common.dto.user.UserDto;
import com.tma.orderservice.client.UserServiceClient;
import com.tma.orderservice.dto.OrderRequest;
import com.tma.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/order")
public class OrderController {

    private final OrderService orderService;

    private final UserServiceClient userServiceClient;

    @PreAuthorize("#oauth2.hasScope('write') && isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String bearerToken) {
        UserDto userDto = userServiceClient.getUserInfo(bearerToken);
        orderService.placeOrder(orderRequest, userDto);
        return "Order placed successfully";
    }

}
