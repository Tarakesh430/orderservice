package com.order.handlers;

import com.common.library.enums.wallet.OrderStatus;
import com.common.library.events.OrderEvent;
import com.common.library.response.wallet.OrderResponse;
import com.order.request.OrderContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DecisionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DecisionHandler.class);
    private final GetWalletOrderGroupHandler getOrderGroupHandler;
    private static final String TARGET_URL = "/v1/cryptotrade/order";

    public void process(OrderContext orderContext) throws Exception {
        OrderEvent requestOrderEvent = orderContext.getRequestOrderEvent();
        String orderGroupId = requestOrderEvent.getOrderGroupId();
        List<OrderResponse> ordersList = getOrderGroupHandler.process(orderGroupId);
        Optional<OrderResponse> lastExecutedOrder =
                ordersList.stream().filter(orderResponse -> orderResponse.getOrderStatus().equals(OrderStatus.PROCESSED)).findFirst();


    }
}
