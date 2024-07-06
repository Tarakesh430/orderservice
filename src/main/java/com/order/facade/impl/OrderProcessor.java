package com.order.facade.impl;

import com.order.facade.AbstractOrderProcessorFacade;
import com.order.handlers.GetSecurityKeysHandler;
import com.order.request.OrderContext;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor extends AbstractOrderProcessorFacade {


    public OrderProcessor(GetSecurityKeysHandler securityKeysHandler) {
        super(securityKeysHandler);
    }

    @Override
    public void publishNextOrder(OrderContext context) {

    }

    @Override
    public void updateOrderStatus(OrderContext context) {

    }

    @Override
    public void processOrder(OrderContext context) {

    }

    @Override
    public void makeDesicion(OrderContext context) {

    }
}
