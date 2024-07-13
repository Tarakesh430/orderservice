package com.order.helper;

import com.common.library.utils.CommonUtils;
import com.order.entity.OrderRecord;
import com.order.enums.OrderRecordStatus;
import com.order.repository.OrderRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRecordHelper {
    private final static Logger logger = LoggerFactory.getLogger(OrderRecordHelper.class);
    private final OrderRecordRepository orderRecordRepository;
    @Transactional
    public OrderRecord updateOrderRecordStatus(String orderRecordId, OrderRecordStatus orderRecordStatus) throws Exception {
        logger.info("Updating the Order Record {} with status {}",orderRecordId,orderRecordStatus);
        OrderRecord orderRecord = orderRecordRepository.findById(orderRecordId).orElseThrow(() -> new Exception("OrderRecord Not Found"));
        orderRecord.setStatus(orderRecordStatus);
        OrderRecord savedOrderRecord = orderRecordRepository.save(orderRecord);
        logger.info("Saved Order Record with {}",savedOrderRecord);
        return savedOrderRecord;
    }


    @Transactional
    public OrderRecord saveOrderRecord(String cryptoOrderId, String walletOrderId, OrderRecordStatus status) {
        OrderRecord orderRecord =new OrderRecord();
        orderRecord.setCryptoOrderId(cryptoOrderId);
        orderRecord.setWalletOrderId(walletOrderId);
        orderRecord.setStatus(status);
        orderRecord.setCreateTime(CommonUtils.getEpochTimeStamp());
        orderRecord.setUpdatedTime(CommonUtils.getEpochTimeStamp());
        OrderRecord savedOrderREcord = orderRecordRepository.save(orderRecord);
        logger.info("Placed the Order Record {}",savedOrderREcord);
        return savedOrderREcord;
    }
}
