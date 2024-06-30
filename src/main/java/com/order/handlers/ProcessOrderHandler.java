package com.order.handlers;

import com.common.library.dto.SecurityKeys;
import com.common.library.events.OrderEvent;
import com.common.library.response.ApiResponse;
import com.order.request.OrderContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class ProcessOrderHandler {
    private final Logger logger = LoggerFactory.getLogger(ProcessOrderHandler.class);

    private final RestClient restClient;
    private static final String TARGET_URL = "/v1/cryptotrade/order";

    @Value("${crypto.trade.api.baseUrl}")
    private String baseUrl;
    public void process(OrderContext orderContext){
        OrderEvent orderEvent = orderContext.getOrderEvent();
        SecurityKeys securityKeys = orderContext.getSecurityKeys();
        ResponseEntity<ApiResponse<SecurityKeys>> response = null;
        try {
            response = restClient.get().uri(getTargetUrl())

                    .retrieve().body(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception ex) {
        }

    }
    public String getTargetUrl() {
        return TARGET_URL;
    }

}
