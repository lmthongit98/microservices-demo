package com.tma.inventoryservice.service;

import com.tma.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {

    List<InventoryResponse> isInStock(List<String> skuCodes);

}
