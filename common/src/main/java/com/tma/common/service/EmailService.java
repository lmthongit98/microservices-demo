package com.tma.common.service;


import com.tma.common.dto.email.EmailId;
import com.tma.common.dto.email.EmailTemplate;

public interface EmailService {
    void sendMail(EmailTemplate emailTemplate, EmailId to, EmailId from, String subject);
}
