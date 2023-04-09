package com.tma.orderservice.dto.email;


public interface MessageTemplate {
    MessageType getMessageType();

    String getTemplateName();
}
