package com.tma.notificationservice.service;

import com.tma.common.dto.email.EmailRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class NotificationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;


    @KafkaListener(id = "${notification.group}", topics = "${notification.topic}")
    public void listen(EmailRequestDto emailRequestDto) {
        try {
            logger.info("START... Sending email for {}", emailRequestDto);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            //load template email with content
            Context context = new Context();
            context.setVariable("name", emailRequestDto.getTo().getFullName());
            context.setVariables(emailRequestDto.getParams());
            String html = templateEngine.process(emailRequestDto.getTemplateName(), context);

            ///send email
            helper.setTo(emailRequestDto.getTo().getEmail());
            helper.setText(html, true);
            helper.setSubject(emailRequestDto.getSubject());
            helper.setFrom(emailRequestDto.getFrom().getEmail());
            javaMailSender.send(message);

            logger.info("END... Email sent successfully.");
        } catch (MessagingException e) {
            logger.error("Email sent with error: " + e.getMessage());
        }
    }


}
