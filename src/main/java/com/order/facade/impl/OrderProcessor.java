package com.order.facade.impl;

import com.common.library.enums.cryptotradeapi.OrderStatus;
import com.common.library.response.cryptotradeapi.OrderResponse;
import com.order.entity.OrderRecord;
import com.order.facade.AbstractOrderProcessorFacade;
import com.order.handlers.DecisionHandler;
import com.order.handlers.GetSecurityKeysHandler;
import com.order.handlers.ProcessOrderHandler;
import com.order.handlers.UpdateOrderHandler;
import com.order.request.OrderContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor extends AbstractOrderProcessorFacade {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);
    @Autowired
    private ProcessOrderHandler processOrderHandler;
    @Autowired
    private UpdateOrderHandler updateOrderHandler;

    @Autowired
    private DecisionHandler decisionHandler;

    public OrderProcessor(GetSecurityKeysHandler securityKeysHandler) {
        super(securityKeysHandler);
    }


    @Override
    public void updateOrderStatus(OrderContext context) {
        OrderResponse cryptoOrderResponse = context.getCryptoOrderResponse();
        //Update the wallet Order status
        if(OrderStatus.OPEN.getValue().equals(cryptoOrderResponse.getStatus())){

        }

    }

    @Override
    public void processOrder(OrderContext context) throws Exception {
        logger.info("Process the order");
        processOrderHandler.process(context);
        // store the Order Record mapping to trigger future events
        OrderRecord orderRecord =new OrderRecord();
    }

    @Override
    public void makeDesicion(OrderContext context) {

    }
}
