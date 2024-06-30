package com.order.kafka;


import com.common.library.events.OrderEvent;
import com.order.kafka.postprocess.BasePostProcess;
import com.order.kafka.postprocess.EventPostProcessHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static final String TOPIC = "order-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EventPostProcessHandlerFactory eventPostProcessFactory;


    public <T> void sendMessage(T message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, message);
        future.thenApply(result -> {
            //Execute Post Process Actions
            Object event = result.getProducerRecord().value();
            if (event instanceof OrderEvent) {
                BasePostProcess<OrderEvent> orderPostProcess = (BasePostProcess<OrderEvent>)
                        eventPostProcessFactory.getHandler("orderEventPostProcess");
                orderPostProcess.postEventProcess((OrderEvent) event);
            }
            return result;
        });
        future.exceptionally(ex -> {
            logger.error("Failed to publish message: {}", message);
            logger.error("Failed with error msg  {}", ex.getMessage());
            return null;
        });
    }
}
