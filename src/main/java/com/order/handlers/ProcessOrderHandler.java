package com.order.handlers;

import com.common.library.dto.SecurityKeys;
import com.common.library.enums.cryptotradeapi.OrderStatus;
import com.common.library.enums.cryptotradeapi.Side;
import com.common.library.events.OrderEvent;
import com.common.library.request.cryptotradeapi.OrderRequest;
import com.common.library.response.ApiResponse;
import com.common.library.response.cryptotradeapi.OrderResponse;
import com.order.request.OrderContext;
import com.order.utils.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProcessOrderHandler {
    private final Logger logger = LoggerFactory.getLogger(ProcessOrderHandler.class);

    private final RestClient restClient;
    private static final String TARGET_URL = "/v1/cryptotrade/order";

    @Value("${crypto.trade.api.baseUrl}")
    private String baseUrl;
    public void process(OrderContext orderContext) throws Exception {
        OrderEvent orderEvent = orderContext.getOrderEvent();
        SecurityKeys securityKeys = orderContext.getSecurityKeys();
        OrderRequest orderRequest = createOrderRequest(orderEvent);
        OrderResponse orderResponse=null;
        try {
            orderResponse = restClient.post().uri(getTargetUrl())
                    .header(CommonConstants.X_API_KEY, securityKeys.getApiKey())
                    .header(CommonConstants.X_SECRET_KEY, securityKeys.getSecretKey())
                    .body(orderRequest)
                    .retrieve().body(new ParameterizedTypeReference<OrderResponse>() {
                    });
        } catch (Exception ex) {
            logger.info("Error in executing a order");
        }
        if(Objects.isNull(orderResponse)|| orderResponse.getStatus().equals(OrderStatus.CANCELLED)){
            logger.info("Order Cancelled pls try again");
            throw new Exception("Order Execution Failed");
        }
        orderContext.setCryptoOrderResponse(orderResponse);
    }

    private OrderRequest createOrderRequest(OrderEvent orderEvent){
        OrderRequest orderRequest =new OrderRequest();
        orderRequest.setExchange(orderEvent.getExchangeName());
        orderRequest.setPrice(orderEvent.getPrice());
        orderRequest.setQuantity(orderEvent.getQuantity());
        orderRequest.setSide(orderEvent.getTradeType());
        orderRequest.setSymbol(orderEvent.getStockName());
        orderRequest.setType("limit");
        return orderRequest;
    }
    public String getTargetUrl() {
        return TARGET_URL;
    }



}
