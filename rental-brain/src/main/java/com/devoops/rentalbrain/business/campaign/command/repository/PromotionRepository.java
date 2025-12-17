package com.devoops.rentalbrain.business.campaign.command.repository;

import com.devoops.rentalbrain.business.campaign.command.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
