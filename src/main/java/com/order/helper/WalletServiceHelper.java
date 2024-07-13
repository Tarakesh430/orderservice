package com.order.helper;

import com.common.library.enums.wallet.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import com.order.handlers.UpdateOrderHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WalletServiceHelper {
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceHelper.class);
    private final ObjectMapper objectMapper;
    private final UpdateOrderHandler updateOrderHandler;
    public List<JsonPatch> getOrderStatusJsonPatch(OrderStatus walletOrderStatus) throws Exception {
        JsonPatch statusUpdateJsonPatch = getJsonPatch("/orderStatus",walletOrderStatus);
        return List.of(statusUpdateJsonPatch);
    }

    private <T> JsonPatch getJsonPatch(String path,T value) throws JsonPointerException {
        JsonPatchOperation jsonPatchOperation = new ReplaceOperation(new JsonPointer(path),
                objectMapper.valueToTree(value));
        return new JsonPatch(Collections.singletonList(jsonPatchOperation));
    }

    public List<JsonPatch> updateWalletExecutedQuantityAndPrice(double executedPrice, double executedQuantity) throws JsonPointerException {
        JsonPatch executedPriceJson = getJsonPatch("/executedPrice", executedPrice);
        JsonPatch executedQuantityJson = getJsonPatch("/executedQuantity", executedQuantity);
        return Arrays.asList(executedPriceJson,executedQuantityJson);
    }
}
