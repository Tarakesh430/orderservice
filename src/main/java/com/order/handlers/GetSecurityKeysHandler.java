package com.order.handlers;

import com.common.library.dto.SecurityKeys;
import com.common.library.response.ApiResponse;
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
public class GetSecurityKeysHandler {
    private final Logger logger = LoggerFactory.getLogger(GetSecurityKeysHandler.class);
    private final RestClient restClient;
    private static final String TARGET_URL = "/v1/wallet/{0}/exchange/{1}/keys";

    @Value("${wallet.api.baseUrl}")
    private String baseUrl;

    public SecurityKeys process(String exchangeName, String walletId) throws Exception {
        logger.info("Get Secret Keys for walletId {} Exchange Name {} ", walletId, exchangeName);
        ResponseEntity<ApiResponse<SecurityKeys>> response = null;
        try {
            response = restClient.get().uri(getTargetUrl(walletId, exchangeName))
                    .retrieve().body(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception ex) {
            logger.info("Exception in retrieving Secret Keys for walletId {} exchange {} ", walletId, exchangeName);
        }
        if(Objects.isNull(response) || !response.getStatusCode().equals(HttpStatus.OK) || Objects.isNull(response.getBody())){
            logger.info("Exception in getting the Security Keys to the given exchangeName {} walletId {}",exchangeName,walletId);
            throw new Exception("Exception in getting the Security Keys to the given exchangeName and walletId");
        }
        return response.getBody().getData();
    }

    public String getTargetUrl(String walletId, String exchangeName) {
        return MessageFormat.format(TARGET_URL, walletId, exchangeName);
    }
}
