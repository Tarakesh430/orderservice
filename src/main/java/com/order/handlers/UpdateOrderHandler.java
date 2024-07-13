package com.order.handlers;

import com.common.library.response.ApiResponse;
import com.common.library.response.wallet.OrderResponse;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UpdateOrderHandler {
    private static final Logger logger = LoggerFactory.getLogger(UpdateOrderHandler.class);

    private final RestClient restClient;
    private static final String TARGET_URL = "/v1/order/{0}/update";
    @Value("${wallet.api.baseUrl}")
    private String baseUrl;

    @Async
    public OrderResponse process(String orderId, List<JsonPatch> updates) throws Exception {
    // Prepare the request and trigger the api call
        ResponseEntity<ApiResponse<OrderResponse>> response = null;
        try {
            logger.info("Send update Order details with json patches {}" ,updates);
            response = restClient.patch().uri(getTargetUrl(orderId))
                    .body(updates)
                    .retrieve().body(new ParameterizedTypeReference<>() {
                    });
            logger.info("Order Status Updated Successfully {}",response);
        } catch (Exception ex) {
            logger.info("Error in executing a order");
            throw new Exception("Exception while updating the order Details " +orderId);
        }
        if(Objects.isNull(response) || !response.getStatusCode().equals(HttpStatus.OK) || Objects.isNull(response.getBody())){
            logger.info("Error in Updating the Wallet Order Details {} {}",orderId,updates);
            throw new Exception("Un able to process the Wallet Order Updates"+orderId);
        }
        return response.getBody().getData();
    }
    public String getTargetUrl(String orderId) {
        return baseUrl.concat(MessageFormat.format(TARGET_URL, orderId));
    }
}
