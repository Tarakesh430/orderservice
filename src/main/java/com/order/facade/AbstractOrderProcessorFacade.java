package com.order.facade;

import com.common.library.dto.SecurityKeys;
import com.common.library.events.OrderEvent;
import com.order.handlers.GetSecurityKeysHandler;
import com.order.request.OrderContext;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public abstract class AbstractOrderProcessorFacade implements OrderFacade {
    private static final Logger logger = LoggerFactory.getLogger(AbstractOrderProcessorFacade.class);
    private final GetSecurityKeysHandler securityKeysHandler;

    @Override
    public void populateSecurityKeys(OrderContext context) throws Exception {
        logger.info("Populate Security Keys");
        OrderEvent requestOrderEvent = context.getRequestOrderEvent();
        String walletId = requestOrderEvent.getWalletId();
        String exchangeName = requestOrderEvent.getExchangeName();
        securityKeysHandler.process(exchangeName,walletId);
        SecurityKeys securityKeys = context.getSecurityKeys();
        if(Objects.isNull(securityKeys) || StringUtils.isBlank(securityKeys.getApiKey()) ||
                StringUtils.isBlank(securityKeys.getSecretKey())){
            throw new Exception("Error in Security Keys");
        }
    }

}