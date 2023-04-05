package com.tma.productservice.service.impl;

import com.tma.productservice.dto.ProductDto;
import com.tma.productservice.model.Product;
import com.tma.productservice.repository.ProductRepository;
import com.tma.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = toEntity(productDto);
        Product savedProduct =  productRepository.save(product);
        log.info("Product is saved with id {}", savedProduct.getId());
        return toDto(savedProduct);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }

    private Product toEntity(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    private ProductDto toDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
