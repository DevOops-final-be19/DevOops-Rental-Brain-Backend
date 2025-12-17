package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyPromotionDTO;

public interface PromotionCommandService {
    String insertPromotion(InsertPromotionDTO promotionDTO);

    String updatePromotion(Long promotionId, ModifyPromotionDTO promotionDTO);

    String deletePromotion(Long promotionId);

    String createPromotionLog(Long promotionId, Long contractId);
}
