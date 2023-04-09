package com.tma.common.dto.email;


public interface MessageTemplate {
    MessageType getMessageType();

    String getTemplateName();
}
