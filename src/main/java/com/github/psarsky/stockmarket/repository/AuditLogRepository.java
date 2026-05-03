package com.github.psarsky.stockmarket.repository;

import com.github.psarsky.stockmarket.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByTimestampAsc();
}
