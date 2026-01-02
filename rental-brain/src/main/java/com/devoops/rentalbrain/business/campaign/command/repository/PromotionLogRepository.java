package com.devoops.rentalbrain.business.campaign.command.repository;

import com.devoops.rentalbrain.business.campaign.command.entity.PromotionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionLogRepository extends JpaRepository<PromotionLog, Long> {
}
