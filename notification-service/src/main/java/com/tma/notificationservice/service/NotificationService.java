package com.tma.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(id = "notificationGroup", topics = "topic")
    public void listen(String msg) {
        logger.info("Received: {}", msg);
    }


}
