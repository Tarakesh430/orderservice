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
    public void validateOrder(OrderContext context) throws Exception {
        logger.info("validate Order Event ");
        OrderEvent orderEvent = Optional.ofNullable(context.getOrderEvent())
                .orElseThrow(() -> new Exception("Order Event should not be null"));
        validateField(orderEvent.getExchangeName(), "Exchange Name");
        validateField(orderEvent.getOrderId(), "Order ID");
        validateField(orderEvent.getWalletId(), "Wallet ID");
        validateField(orderEvent.getTradeType(), "Trade Type");
        validateField(orderEvent.getStockName(), "Stock Name");
        validateFieldNonNegative(orderEvent.getPrice(), "Price");
        validateFieldNonNegative(orderEvent.getQuantity(), "Quantity");

    }

    private void validateField(String field, String fieldName) throws Exception {
        if (StringUtils.isBlank(field)) {
            throw new Exception(fieldName + " should not be null");
        }
    }

    private void validateFieldNonNegative(double field, String fieldName) throws Exception {
        if (field <= 0) {
            throw new Exception(fieldName + " should not be negative");
        }
    }

    @Override
    public void populateSecurityKeys(OrderContext context) throws Exception {
        logger.info("Populate Security Keys");
        securityKeysHandler.process(context);
        SecurityKeys securityKeys = context.getSecurityKeys();
        if(Objects.isNull(securityKeys) || StringUtils.isBlank(securityKeys.getApiKey()) ||
                StringUtils.isBlank(securityKeys.getSecretKey())){
            throw new Exception("Error in Security Keys");
        }
    }

}