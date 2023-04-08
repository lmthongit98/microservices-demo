package com.tma.productservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String skuCode;
    private String name;
    private String description;
    private BigDecimal price;
}
