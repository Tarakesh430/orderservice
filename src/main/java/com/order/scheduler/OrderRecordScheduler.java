package com.order.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderRecordScheduler {
    private static final Logger logger = LoggerFactory.getLogger(OrderRecordScheduler.class);
    @Scheduled(cron = "2 * * * * *")
    @Transactional
    public void performTask() {
    }
}
