package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.PromotionDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionWithContractDTO;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

import java.util.List;

public interface PromotionQueryService {
    PageResponseDTO<PromotionDTO> readPromotionList(Criteria criteria);

    PageResponseDTO<PromotionDTO> searchPromotion(String keyword, Criteria criteria);

    PageResponseDTO<PromotionDTO> filterPromotionByType(String type, Criteria criteria);

    PageResponseDTO<PromotionDTO> filterPromotionByStatus(String status, Criteria criteria);

    List<PromotionWithContractDTO> useContractPromotion(Integer segment);

    PromotionDTO readDetailPromotion(String proCode);
}
