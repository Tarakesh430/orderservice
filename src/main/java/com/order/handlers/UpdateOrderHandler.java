package com.order.handlers;

import com.common.library.enums.cryptotradeapi.OrderStatus;
import com.common.library.request.wallet.OrderRequest;
import com.common.library.response.wallet.OrderResponse;
import com.github.fge.jsonpatch.JsonPatch;
import com.order.request.OrderContext;
import com.order.utils.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateOrderHandler {
    private static final Logger logger = LoggerFactory.getLogger(UpdateOrderHandler.class);

    private final RestClient restClient;
    private static final String TARGET_URL = "/v1/order/{0}/update";


    @Async
    public void process(String orderId, List<JsonPatch> updates) throws Exception {
    // Prepare the request and trigger the api call
        try {
            logger.info("Send update Order details with json patches {}" ,updates);
            OrderResponse orderResponse = restClient.patch().uri(getTargetUrl(orderId))
                    .body(updates)
                    .retrieve().body(new ParameterizedTypeReference<>() {
                    });
            logger.info("Order Status Updated Successfully {}",orderResponse);
        } catch (Exception ex) {
            logger.info("Error in executing a order");
            throw new Exception("Exception while updating the order Details " +orderId);
        }
    }
    public String getTargetUrl(String orderId) {
        return MessageFormat.format(TARGET_URL, orderId);
    }
}
