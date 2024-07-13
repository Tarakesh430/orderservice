package com.order.handlers;

import com.common.library.response.ApiResponse;
import com.common.library.response.wallet.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetWalletOrderDetailsHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetWalletOrderDetailsHandler.class);
    private final RestClient restClient;
    @Value("${wallet.api.baseUrl}")
    private String baseUrl;
    private static final String TARGET_URL = "v1/order/{0}";
    public OrderResponse process(String walletOrderId) throws Exception {
        logger.info("Get Cryoto Order Details from Trade API {}", walletOrderId);
        ResponseEntity<ApiResponse<com.common.library.response.wallet.OrderResponse>> response = null;
        try {
            response = restClient.get().uri(getTargetUrl(walletOrderId))
                    .retrieve().body(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception ex) {
            logger.info("Exception in getting the Wallet OrderDetails {}", walletOrderId);
            throw new Exception("Exception in getting the Wallet  OrderDetails {}", ex);
        }
        if (Objects.isNull(response) || Objects.isNull(response.getBody()) ||
                !response.getStatusCode().equals(HttpStatus.OK)) {
            logger.info("Invalid Response while getting the Wallet Order Details");
            throw new Exception("Invalid Response while getting the Wallet Order Details");
        }
        logger.info("SuccessFully Retrieved the ");
        return response.getBody().getData();
    }
    public String getTargetUrl(String walletOrderId) {
        return baseUrl.concat(MessageFormat.format(TARGET_URL, walletOrderId));
    }
}
