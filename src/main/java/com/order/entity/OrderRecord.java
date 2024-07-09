package com.order.entity;

import com.order.enums.OrderRecordStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "order_records")
@Table(name = "order_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderRecordUUid;
    private String walletOrderId;
    private String cryptoOrderId;
    @Column(name = "order_record_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderRecordStatus status;
    private long createTime;
    private long updatedTime;
}
