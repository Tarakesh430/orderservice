package com.order.handlers;

import com.common.library.dto.SecurityKeys;
import com.common.library.response.ApiResponse;
import com.common.library.response.cryptotradeapi.OrderResponse;
import com.order.utils.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetCrypotOrderDetailsHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetCrypotOrderDetailsHandler.class);
    private final RestClient restClient;
    private final String TARGET_URL="/v1/cryptotrade/order?global-order-id={0}";

    public OrderResponse process(String cryptoOrderId, SecurityKeys securityKeys) throws Exception {
        logger.info("Get Cryoto Order Details from Trade API {}", cryptoOrderId);
        ResponseEntity<ApiResponse<OrderResponse>> response = null;
        try {
            response = restClient.get().uri(getTargetUrl(cryptoOrderId))
                    .header(CommonConstants.X_API_KEY, securityKeys.getApiKey())
                    .header(CommonConstants.X_SECRET_KEY, securityKeys.getSecretKey())
                    .retrieve().body(new ParameterizedTypeReference<ResponseEntity<ApiResponse<OrderResponse>>>() {
                    });
        } catch (Exception ex) {
            logger.info("Exception in getting the Crypto OrderDetails {}", cryptoOrderId);
            throw new Exception("Exception in getting the Crypto OrderDetails {}", ex);
        }
        if (Objects.isNull(response) || Objects.isNull(response.getBody()) ||
                !response.getStatusCode().equals(HttpStatus.OK)) {
             logger.info("Invalid Response while getting the Order Details");
             throw new Exception("Invalid Response while getting the Order Details");
        }
        logger.info("SuccessFully Retrieved the ");
        return response.getBody().getData();
    }
    public String getTargetUrl(String cryptoOrderId) {
        return MessageFormat.format(TARGET_URL, cryptoOrderId);
    }
}