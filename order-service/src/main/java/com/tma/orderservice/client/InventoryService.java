package com.tma.orderservice.client;

import com.tma.orderservice.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "inventory-service", url = "http://localhost:8083")
public interface InventoryService {

    @GetMapping("/api/inventory/{sku-code}")
    List<InventoryResponse> isInStock(@PathVariable("sku-code") List<String> skuCodes);

}
