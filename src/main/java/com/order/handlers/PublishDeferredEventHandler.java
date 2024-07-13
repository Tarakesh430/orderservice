package com.order.handlers;

import com.common.library.request.wallet.DeferredEventRequest;
import com.common.library.response.ApiResponse;
import com.common.library.response.wallet.DeferredEventResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@RequiredArgsConstructor
public class PublishDeferredEventHandler {
    private final Logger logger = LoggerFactory.getLogger(PublishDeferredEventHandler.class);

    private final RestClient restClient;
    private static final String TARGET_URL = "/v1/deferredevent";
    @Value("${wallet.api.baseUrl}")
    private String baseUrl;

    public void process(DeferredEventRequest deferredEventRequest) throws Exception {
        try {
            logger.info("Create a deferred Event with request {}" ,deferredEventRequest);
            ResponseEntity<ApiResponse<DeferredEventResponse>> response = restClient.post().uri(getTargetUrl())
                    .body(deferredEventRequest)
                    .retrieve().body(new ParameterizedTypeReference<ResponseEntity<ApiResponse<DeferredEventResponse>>>() {
                    });
            logger.info("Deferred  Event Created with {}",response);
        } catch (Exception ex) {
            logger.info("Error in while creating a deferred event");
            throw new Exception("Exception while creating a deferred event " + deferredEventRequest);
        }

    }
    public String getTargetUrl() {
        return baseUrl.concat(TARGET_URL);
    }

}
