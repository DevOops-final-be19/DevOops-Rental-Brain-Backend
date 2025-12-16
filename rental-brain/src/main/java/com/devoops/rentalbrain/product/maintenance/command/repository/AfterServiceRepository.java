package com.devoops.rentalbrain.product.maintenance.command.repository;

import com.devoops.rentalbrain.product.maintenance.command.entity.AfterService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AfterServiceRepository extends JpaRepository<AfterService, Long> {

    // 동일 자산 + 동일 예정일 중복 방지
    boolean existsByItemIdAndDueDate(Long itemId, LocalDateTime dueDate);

    // 예정 상태 + 기한 경과
    List<AfterService> findByStatusAndDueDateBefore(String status, LocalDateTime now);

}