package com.tma.notificationservice.dto.email;


public interface MessageTemplate {
    MessageType getMessageType();

    String getTemplateName();
}
