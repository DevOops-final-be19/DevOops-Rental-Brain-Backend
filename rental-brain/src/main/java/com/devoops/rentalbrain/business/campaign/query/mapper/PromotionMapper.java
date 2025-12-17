package com.devoops.rentalbrain.business.campaign.query.mapper;

import com.devoops.rentalbrain.business.campaign.query.dto.PromotionDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionWithContractDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PromotionMapper {
    List<PromotionDTO> selectPromotion(int offset, int amount);

    long countPromotionList();

    List<PromotionDTO> searchPromotion(String keyword, int offset, int amount);

    long countSearchedPromotion(String keyword);

    List<PromotionDTO> filteringPromotionByType(String type, int offset, int amount);

    long countFliteringPromotion(String type);

    List<PromotionDTO> filteringPromotionByStatus(String status, int offset, int amount);

    long countFliterPromotionByStatus(String status);

    List<PromotionWithContractDTO> useContractPromotion(String segment);
}
