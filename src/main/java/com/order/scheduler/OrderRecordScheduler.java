package com.order.scheduler;

import com.order.entity.OrderRecord;
import com.order.enums.OrderRecordStatus;
import com.order.repository.OrderRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRecordScheduler {
    private static final Logger logger = LoggerFactory.getLogger(OrderRecordScheduler.class);
    private final OrderRecordRepository orderRecordRepository;
    @Scheduled(cron = "2 * * * * *")
    @Transactional
    public void performTask() {
        logger.info("Get all the Order Records with Status PLACED,RETRIGGER");
        List<OrderRecord> orderRecords = orderRecordRepository
                .findByStatusIn(Arrays.asList(OrderRecordStatus.PLACED, OrderRecordStatus.RETRIGGER));
        logger.info("Placing Deferred Orders for the orders list size {}",orderRecords.size());
        for(OrderRecord  orderRecord : orderRecords){
            if(orderRecord.getStatus().equals(OrderRecordStatus.PLACED)){
                //get the order status and if it is executed, move the status to COMPLETED or CANCELLED
                //trigger a request with new order deferred for 10 min
            }else if(orderRecord.getStatus().equals(OrderRecordStatus.RETRIGGER)){
                //Place a new order with updated prices and fdefer fopr next 5 min
                //Move the current status to NO_ACTION_COMPLETED
            }
        }

    }
}
