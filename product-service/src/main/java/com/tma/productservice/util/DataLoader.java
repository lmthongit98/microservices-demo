package com.tma.productservice.util;

import com.tma.productservice.model.Product;
import com.tma.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Product product1 = Product.builder()
                .skuCode("iphone_13")
                .name("Iphone 13")
                .description("iPhone 13 thuộc phân khúc điện thoại cao cấp mà không một iFan nào có thể bỏ qua..")
                .price(BigDecimal.valueOf(30000000))
                .build();

        Product product2 = Product.builder()
                .skuCode("iphone_13")
                .name("Iphone 14")
                .description("iPhone 14 thuộc phân khúc điện thoại cao cấp mà không một iFan nào có thể bỏ qua..")
                .price(BigDecimal.valueOf(35000000))
                .build();

        Product product3 = Product.builder()
                .skuCode("samsung_galaxy_s23")
                .name("Samsung Galaxy S23")
                .description("Samsung Galaxy S23 5G 256GB có lẽ là ứng cử viên sáng giá được xướng tên trong danh sách điện thoại đáng mua nhất năm 2023..")
                .price(BigDecimal.valueOf(3300000))
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

    }

}
