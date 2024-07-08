package com.order.consumer;

import com.common.library.events.OrderEvent;
import com.order.request.OrderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerListener {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);

    @KafkaListener(topics = "order-topic", groupId = "group_id")
    public void consume(Object message) {
        if(message instanceof OrderEvent){
            logger.info("Received a ");
            OrderEvent orderEvent = (OrderEvent) message;
            OrderContext orderContext = OrderContext.builder().requestOrderEvent(orderEvent).build();
        }
    }
}