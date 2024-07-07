package com.order.handlers;

import com.order.request.OrderContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class DecisionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DecisionHandler.class);

    private final RestClient restClient;
    private static final String TARGET_URL = "/v1/cryptotrade/order";

    @Value("${crypto.trade.api.baseUrl}")
    private String baseUrl;
    public void process(OrderContext orderContext) throws Exception {

    }
}
