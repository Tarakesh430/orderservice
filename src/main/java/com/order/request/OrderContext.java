package com.order.request;


import com.common.library.dto.SecurityKeys;
import com.common.library.enums.TradeType;
import com.common.library.events.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderContext {
    private OrderEvent orderEvent;
    private String notificationMessage;
    private SecurityKeys securityKeys;
    private Map<String,Object> data;

    // Add any additional fields as required
}