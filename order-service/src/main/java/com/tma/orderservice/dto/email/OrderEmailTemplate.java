package com.tma.orderservice.dto.email;

import com.tma.orderservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class OrderEmailTemplate extends EmailTemplate {

    private String templateName;

    private Order order;



    @Override
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
