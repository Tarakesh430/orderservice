package com.order.facade.impl;

import com.common.library.enums.wallet.OrderStatus;
import com.common.library.events.OrderEvent;
import com.common.library.response.cryptotradeapi.OrderResponse;
import com.common.library.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import com.order.entity.OrderRecord;
import com.order.enums.OrderRecordStatus;
import com.order.facade.AbstractOrderProcessorFacade;
import com.order.handlers.DecisionHandler;
import com.order.handlers.GetSecurityKeysHandler;
import com.order.handlers.ProcessOrderHandler;
import com.order.handlers.UpdateOrderHandler;
import com.order.repository.OrderRecordRepository;
import com.order.request.OrderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Objects;

import static jdk.jfr.internal.consumer.StringParser.Encoding.EMPTY_STRING;

@Component
public class OrderProcessor extends AbstractOrderProcessorFacade {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);
    @Autowired
    private ProcessOrderHandler processOrderHandler;
    @Autowired
    private UpdateOrderHandler updateOrderHandler;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DecisionHandler decisionHandler;
    @Autowired
    private OrderRecordRepository orderRecordRepository;

    public OrderProcessor(GetSecurityKeysHandler securityKeysHandler) {
        super(securityKeysHandler);
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
        JsonPatchOperation jsonPatchOperation = new ReplaceOperation(new JsonPointer("/orderStatus"),
                objectMapper.valueToTree(OrderStatus.INPROGRESS));
        JsonPatch statusUpdateJsonPatch = new JsonPatch(Collections.singletonList(jsonPatchOperation));
        updateOrderHandler.process(requestOrderEvent.getOrderId(), Collections.singletonList(statusUpdateJsonPatch));
        logger.info("Process Status Update for the Wallet Order Id to INPROGRESS");
    }

    @Override
    public void processOrder(OrderContext context) throws Exception {
        logger.info("Process the order");
        processOrderHandler.process(context);
        OrderResponse cryptoOrderResponse = context.getCryptoOrderResponse();
        OrderEvent requestOrderEvent = context.getRequestOrderEvent();
        // store the Order Record mapping to trigger future events
        saveOrderRecord(cryptoOrderResponse.getGlobalOrderUid(), requestOrderEvent.getOrderId(),OrderRecordStatus.PLACED);
    }

    private void saveOrderRecord(String cryptoOrderId, String walletOrderId,OrderRecordStatus status) {
        OrderRecord orderRecord =new OrderRecord();
        orderRecord.setCryptoOrderId(cryptoOrderId);
        orderRecord.setWalletOrderId(walletOrderId);
        orderRecord.setStatus(status);
        orderRecord.setCreateTime(CommonUtils.getEpochTimeStamp());
        orderRecord.setUpdatedTime(CommonUtils.getEpochTimeStamp());
        OrderRecord savedOrderREcord = orderRecordRepository.save(orderRecord);
        logger.info("Placed the Order Record {}",savedOrderREcord);
    }

    @Override
    public void makeDesicion(OrderContext context) throws Exception {
        decisionHandler.process(context);
        OrderEvent requestOrderEvent = context.getRequestOrderEvent();
        if(true){
            //Proceed with order
        }else{
            //Add the wallet Order Id with Retrigger status to reinitiate the request after some time
            saveOrderRecord("", requestOrderEvent.getOrderId(), OrderRecordStatus.RETRIGGER);
        }
    }
}
