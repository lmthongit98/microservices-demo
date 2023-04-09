package com.tma.orderservice.dto.email;

import lombok.Data;

import java.util.Map;

@Data
public class EmailRequestDto {
    Map<String, Object> params;
    private String subject;
    private EmailId from;
    private EmailId to;
    private String templateName;
}