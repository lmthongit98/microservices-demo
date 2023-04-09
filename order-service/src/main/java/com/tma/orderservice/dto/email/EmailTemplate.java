package com.tma.orderservice.dto.email;

public abstract class EmailTemplate implements MessageTemplate {

    public EmailTemplate() {
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.EMAIL;
    }
}
