package com.tma.orderservice.service;

import com.tma.orderservice.dto.email.EmailId;
import com.tma.orderservice.dto.email.EmailTemplate;

public interface EmailService {
    void sendMail(EmailTemplate emailTemplate, EmailId to, EmailId from, String subject);
}
