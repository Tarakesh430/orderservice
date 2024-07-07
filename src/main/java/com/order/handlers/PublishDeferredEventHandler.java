package com.order.handlers;

import com.common.library.response.cryptotradeapi.OrderResponse;
import com.order.request.OrderContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@RequiredArgsConstructor
public class PublishDeferredEventHandler {
    private final Logger logger = LoggerFactory.getLogger(PublishDeferredEventHandler.class);

    private final RestClient restClient;
    private static final String TARGET_URL = "/v1/cryptotrade/order";

    public void process(OrderContext orderContext) throws Exception {
        OrderResponse orderResponse = orderContext.getCryptoOrderResponse();

        //Check if the current order placed or else

    }
}
