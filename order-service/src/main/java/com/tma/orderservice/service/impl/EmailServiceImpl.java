package com.tma.orderservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.orderservice.dto.email.EmailId;
import com.tma.orderservice.dto.email.EmailRequestDto;
import com.tma.orderservice.dto.email.EmailTemplate;
import com.tma.orderservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${notification.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, EmailRequestDto> kafkaTemplate;

    private static final TypeReference<Map<String, Object>> paramsTypeRef = new TypeReference<>() {
    };

    @Override
    public void sendMail(EmailTemplate emailTemplate, EmailId to, EmailId from, String subject) {
        EmailRequestDto emailRequestDto = new EmailRequestDto();
        emailRequestDto.setTemplateName(emailTemplate.getTemplateName());
        emailRequestDto.setSubject(subject);
        emailRequestDto.setTo(to);
        emailRequestDto.setFrom(from);
        Map<String, Object> params = new ObjectMapper().convertValue(emailTemplate, paramsTypeRef);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            this.convertParamEntryToString(stringObjectEntry, params);
        }
        emailRequestDto.setParams(params);
        kafkaTemplate.send(topic, emailRequestDto);
    }


    private void convertParamEntryToString(Map.Entry<String, Object> paramEntry, Map<String, Object> params) {
        Object mapValue = paramEntry.getValue();
        if (mapValue != null) {
            String convertedValue;
            if (!(mapValue instanceof Integer) && !(mapValue instanceof Long)) {
                if (mapValue instanceof Double || mapValue instanceof Float) {
                    convertedValue = String.format("%.2f", mapValue);
                    params.put(paramEntry.getKey(), convertedValue);
                }
            } else {
                convertedValue = mapValue.toString();
                params.put(paramEntry.getKey(), convertedValue);
            }
        }

    }

}
