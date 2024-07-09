package com.order.enums;

import com.order.entity.OrderRecord;

import java.util.Arrays;
import java.util.Objects;

public enum OrderRecordStatus {
    OPEN("OPEN"),
    PLACED("PLACED"),
    CANCELLED("CANCELLED"),
    EXECUTED("EXECUTED"),
    RETRIGGER("RETRIGGER"),
    NO_ACTION_COMPLETED("NO_ACTION_COMPLETED");

    private final String value;

    private OrderRecordStatus(String value) {
        this.value = value;
    }

    public static OrderRecordStatus fromString(String orderRecordStatus) {
        return  Arrays.stream(values()).filter((status) -> status.getValue().equalsIgnoreCase(orderRecordStatus))
                .findFirst().orElse( null);
    }

    public static boolean in(String orderRecordStatus) {
        return Objects.nonNull(fromString(orderRecordStatus));
    }

    public String getValue() {
        return this.value;
    }
}

