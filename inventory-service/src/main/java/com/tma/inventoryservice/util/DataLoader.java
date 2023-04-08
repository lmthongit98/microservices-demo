package com.tma.inventoryservice.util;

import com.tma.inventoryservice.model.Inventory;
import com.tma.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;
    @Override
    public void run(String... args) throws Exception {
        Inventory inventory1 = new Inventory();
        inventory1.setSkuCode("iphone_13");
        inventory1.setQuantity(100);

        Inventory inventory2 = new Inventory();
        inventory2.setSkuCode("iphone_14");
        inventory2.setQuantity(0);

        Inventory inventory3 = new Inventory();
        inventory3.setSkuCode("samsung_galaxy_s23");
        inventory3.setQuantity(50);

        inventoryRepository.saveAll(List.of(inventory1, inventory2, inventory3));
    }
}

