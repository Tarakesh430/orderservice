package com.order.repository;

import com.order.entity.OrderRecord;
import com.order.enums.OrderRecordStatus;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRecordRepository extends JpaRepository<OrderRecord,String> {
    @Query("SELECT o FROM OrderRecord o WHERE o.status IN :status")
    List<OrderRecord> findByStatusIn(@Param("status") List<OrderRecordStatus> orderStatus);

}
