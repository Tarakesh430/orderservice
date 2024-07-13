package com.order.facade.impl;

import com.common.library.enums.wallet.OrderStatus;
import com.common.library.events.OrderEvent;
import com.common.library.response.cryptotradeapi.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.order.enums.OrderRecordStatus;
import com.order.facade.AbstractOrderProcessorFacade;
import com.order.handlers.DecisionHandler;
import com.order.handlers.GetSecurityKeysHandler;
import com.order.handlers.ProcessOrderHandler;
import com.order.handlers.UpdateOrderHandler;
import com.order.helper.OrderRecordHelper;
import com.order.helper.WalletServiceHelper;
import com.order.request.OrderContext;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
public class OrderProcessor extends AbstractOrderProcessorFacade {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);
    @Autowired
    private ProcessOrderHandler processOrderHandler;
    @Autowired
    private UpdateOrderHandler updateOrderHandler;

    @Autowired
    OrderRecordHelper orderRecordHelper;

    @Autowired
    WalletServiceHelper walletServiceHelper;
    @Autowired
    private DecisionHandler decisionHandler;

    public OrderProcessor(GetSecurityKeysHandler securityKeysHandler) {
        super(securityKeysHandler);
    }
    @Override
    public void validateOrder(OrderContext context) throws Exception {
        logger.info("validate Order Event ");
        OrderEvent orderEvent = Optional.ofNullable(context.getRequestOrderEvent())
                .orElseThrow(() -> new Exception("Order Event should not be null"));
        validateField(orderEvent.getExchangeName(), "Exchange Name");
        validateField(orderEvent.getOrderId(), "Order ID");
        validateField(orderEvent.getWalletId(), "Wallet ID");
        validateField(orderEvent.getTradeType(), "Trade Type");
        validateField(orderEvent.getStockName(), "Stock Name");
        validateFieldNonNegative(orderEvent.getPrice(), "Price");
        validateFieldNonNegative(orderEvent.getQuantity(), "Quantity");
    }

    private void validateField(String field, String fieldName) throws Exception {
        if (StringUtils.isBlank(field)) {
            throw new Exception(fieldName + " should not be null");
        }
    }

    private void validateFieldNonNegative(double field, String fieldName) throws Exception {
        if (field <= 0) {
            throw new Exception(fieldName + " should not be negative");
        }
    }



    @Override
    public void updateOrderStatus(OrderContext context) throws Exception {
        OrderResponse cryptoOrderResponse = context.getCryptoOrderResponse();
        OrderEvent requestOrderEvent = context.getRequestOrderEvent();
        if(Objects.isNull(cryptoOrderResponse)){
            logger.info("Error in placing the Order for Event {}",requestOrderEvent);
            throw new Exception("Error in placing the order to Crypto Trade api");
        }
        //Update the wallet Order status
        List<JsonPatch> statusUpdateJsonPatch = walletServiceHelper.getOrderStatusJsonPatch(OrderStatus.INPROGRESS);
        updateOrderHandler.process(requestOrderEvent.getOrderId(), statusUpdateJsonPatch);
        logger.info("Process Status Update for the Wallet Order Id to INPROGRESS");
    }

    @Override
    public void processOrder(OrderContext context) throws Exception {
        logger.info("Process the order");
        processOrderHandler.process(context);
        OrderResponse cryptoOrderResponse = context.getCryptoOrderResponse();
        OrderEvent requestOrderEvent = context.getRequestOrderEvent();
        // store the Order Record mapping to trigger future events
        orderRecordHelper.saveOrderRecord(cryptoOrderResponse.getGlobalOrderUid(), requestOrderEvent.getOrderId(),OrderRecordStatus.PLACED);
    }


    @Override
    public void makeDesicion(OrderContext context) throws Exception {
        decisionHandler.process(context);
        OrderEvent requestOrderEvent = context.getRequestOrderEvent();
        if(true){
            //Proceed with order
        }else{
            //Add the wallet Order Id with Retrigger status to reinitiate the request after some time
            orderRecordHelper.saveOrderRecord("", requestOrderEvent.getOrderId(), OrderRecordStatus.RETRIGGER);
        }
    }
}
