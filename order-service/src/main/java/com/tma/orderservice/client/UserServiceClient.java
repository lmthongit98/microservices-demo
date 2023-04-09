package com.tma.orderservice.client;

import com.tma.common.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/user/info")
    UserDto getUserInfo(@RequestHeader("Authorization") String bearerToken);

}
