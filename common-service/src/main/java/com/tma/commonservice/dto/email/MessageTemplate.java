package com.tma.commonservice.dto.email;


public interface MessageTemplate {
    MessageType getMessageType();

    String getTemplateName();
}
