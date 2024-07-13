package com.order.scheduler;

import com.common.library.dto.SecurityKeys;
import com.common.library.enums.TradeType;
import com.common.library.enums.cryptotradeapi.OrderStatus;
import com.common.library.enums.wallet.deferredevent.EventType;
import com.common.library.request.wallet.DeferredEventRequest;
import com.common.library.request.wallet.OrderRequest;
import com.common.library.response.wallet.OrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.order.entity.OrderRecord;
import com.order.enums.OrderRecordStatus;
import com.order.handlers.GetCrypotOrderDetailsHandler;
import com.order.handlers.GetSecurityKeysHandler;
import com.order.handlers.GetWalletOrderDetailsHandler;
import com.order.handlers.UpdateOrderHandler;
import com.order.helper.OrderRecordHelper;
import com.order.helper.WalletServiceHelper;
import com.order.repository.OrderRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRecordScheduler {
    private static final Logger logger = LoggerFactory.getLogger(OrderRecordScheduler.class);
    private final OrderRecordRepository orderRecordRepository;
    private final GetCrypotOrderDetailsHandler cryptoOrderDetailsHandler;
    private final GetWalletOrderDetailsHandler walletOrderDetailsHandler;
    private final GetSecurityKeysHandler securityKeysHandler;
    private final WalletServiceHelper walletServiceHelper;
    private final OrderRecordHelper orderRecordHelper;
    private final ObjectMapper objectMapper;
    private final UpdateOrderHandler updateOrderHandler;

    @Scheduled(cron = "2 * * * * *")
    @Transactional
    public void performTask() throws Exception {
        logger.info("Get all the Order Records with Status PLACED,RETRIGGER");
        List<OrderRecord> orderRecords = orderRecordRepository
                .findByStatusIn(Arrays.asList(OrderRecordStatus.PLACED, OrderRecordStatus.RETRIGGER));
        logger.info("Placing Deferred Orders for the orders list size {}", orderRecords.size());
        for (OrderRecord orderRecord : orderRecords) {
            OrderResponse walletOrderResponse = walletOrderDetailsHandler.process(orderRecord.getWalletOrderId());
            SecurityKeys securityKeys = securityKeysHandler.process(walletOrderResponse.getExchangeName(),
                    walletOrderResponse.getWalletId());
            if (orderRecord.getStatus().equals(OrderRecordStatus.PLACED)) {
                //get the order status and if it is executed, move the status to COMPLETED or CANCELLED
                com.common.library.response.cryptotradeapi.OrderResponse cryptoOrderResponse =
                        cryptoOrderDetailsHandler.process(orderRecord.getCryptoOrderId(), securityKeys);
                if (cryptoOrderResponse.getStatus().equals(OrderStatus.EXECUTED)) {
                    //Change the wallet Order Status to Completed. and the OrderRecord Status to Completed and publish a next deferred event order
                    List<JsonPatch> walletOrderJsonPatch = walletServiceHelper.getOrderStatusJsonPatch(com.common.library.enums.wallet.OrderStatus.PROCESSED);
                    walletOrderJsonPatch.addAll(walletServiceHelper.updateWalletExecutedQuantityAndPrice(Double.parseDouble(cryptoOrderResponse.getAveragePrice()),
                            Double.parseDouble(cryptoOrderResponse.getExecutedQty())));
                    updateOrderHandler.process(walletOrderResponse.getOrderId(),walletOrderJsonPatch);
                    orderRecordHelper.updateOrderRecordStatus(orderRecord.getOrderRecordUUid()
                            , OrderRecordStatus.EXECUTED);


                } else if (cryptoOrderResponse.getStatus().equals(OrderStatus.CANCELLED)) {
                    //Change the Order Record status to Cancelled and update the wallet Order Status to Cancelled and publish a next deferred event order
                    walletServiceHelper.getOrderStatusJsonPatch(com.common.library.enums.wallet.OrderStatus.CANCELLED);
                    orderRecordHelper.updateOrderRecordStatus(orderRecord.getOrderRecordUUid()
                            , OrderRecordStatus.CANCELLED);
                }
                //trigger a request with new order deferred for 10 min
            } else if (orderRecord.getStatus().equals(OrderRecordStatus.RETRIGGER)) {
                //Place a new order with updated prices and defer for next 5 min
                //Move the current status to NO_ACTION_COMPLETED
                walletServiceHelper.getOrderStatusJsonPatch(com.common.library.enums.wallet.OrderStatus.CANCELLED);
                orderRecordHelper.updateOrderRecordStatus(orderRecord.getOrderRecordUUid()
                        , OrderRecordStatus.NO_ACTION_COMPLETED);
            }
        }
    }


    public OrderRequest prepareOrderRequest(String orderGroupId, String walletId, String exchangeName, String stockName,
                                        TradeType tradeType, double price, double quantity ) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderGroupId(orderGroupId);
        orderRequest.setWalletId(walletId);
        orderRequest.setPrice(price);
        orderRequest.setExchangeName(exchangeName);
        orderRequest.setTradeType(tradeType);
        orderRequest.setQuantity(quantity);
        orderRequest.setRecurring(false);
        orderRequest.setStockName(stockName);
        return orderRequest;
    }

    public DeferredEventRequest createDeferredEventRequest(OrderRequest orderRequest) throws JsonProcessingException {
        DeferredEventRequest deferredEvent = new DeferredEventRequest();
        deferredEvent.setDeferUntil(getFutureTimeStamp(5));
        deferredEvent.setEventSource("OrderService");
        deferredEvent.setEventType(EventType.ORDER_CREATION_EVENT.getValue());
        deferredEvent.setPayload(objectMapper.writeValueAsString(orderRequest));
        return deferredEvent;
    }

    public static long getFutureTimeStamp(int minutes) {
        LocalDateTime futureTime = LocalDateTime.now().plusMinutes(minutes);
        return futureTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}
