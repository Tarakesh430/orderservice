package com.order.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerListener {

    @KafkaListener(topics = "order-topic", groupId = "group_id")
    public void consume(Object message) {
        System.out.println("Consumed message: " + message);
    }
}