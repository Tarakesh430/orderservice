package com.order.handlers;

import com.order.request.OrderContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class UpdateOrderHandler {
    private static final Logger logger = LoggerFactory.getLogger(UpdateOrderHandler.class);

    private final RestClient restClient;

    public void process(OrderContext orderContext){
        logger.info("UpdateOrder Details");
    }
}
