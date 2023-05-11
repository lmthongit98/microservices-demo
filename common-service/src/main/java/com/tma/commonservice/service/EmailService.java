package com.tma.commonservice.service;


import com.tma.commonservice.dto.email.EmailId;
import com.tma.commonservice.dto.email.EmailTemplate;

public interface EmailService {
    void sendMail(EmailTemplate emailTemplate, EmailId to, EmailId from, String subject);
}
