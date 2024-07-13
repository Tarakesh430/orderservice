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
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetWalletOrderGroupHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetWalletOrderDetailsHandler.class);
    private final RestClient restClient;
    private static final String TARGET_URL = "/order-group/{0}/getOrders";

    @Value("${wallet.api.baseUrl}")
    private String baseUrl;
    public List<OrderResponse> process(String orderGroupId) throws Exception {
        logger.info("Get all the Order associated with the Order Group Id {}",orderGroupId);
        ResponseEntity<ApiResponse<List<OrderResponse>>> response = null;
        try {
            response = restClient.get().uri(getTargetUrl(orderGroupId))
                    .retrieve().body(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception ex) {
            logger.info("Exception in getting the Wallet OrderGroup Details {}", orderGroupId);
            throw new Exception("Exception in getting the Wallet  OrderDetails {}", ex);
        }
        if (Objects.isNull(response) || Objects.isNull(response.getBody()) ||
                !response.getStatusCode().equals(HttpStatus.OK)) {
            logger.info("Invalid Response while getting the Wallet Order Group Details");
            throw new Exception("Invalid Response while getting the Wallet Order Group Details");
        }
        logger.info("SuccessFully Retrieved the ");
        return response.getBody().getData();
    }

    private String getTargetUrl(String orderGroupId){
        return baseUrl.concat(MessageFormat.format(TARGET_URL,orderGroupId));
    }
}
