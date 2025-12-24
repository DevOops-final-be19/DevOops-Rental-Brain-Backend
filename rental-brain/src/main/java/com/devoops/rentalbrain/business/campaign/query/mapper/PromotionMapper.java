package com.devoops.rentalbrain.business.campaign.query.mapper;

import com.devoops.rentalbrain.business.campaign.query.dto.PromotionDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionWithContractDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionMapper {
    List<PromotionDTO> selectPromotion(@Param("offset") int offset, @Param("limit") int limit);

    long countPromotionList();

    List<PromotionDTO> searchPromotion(String keyword,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    long countSearchedPromotion(String keyword);

    List<PromotionDTO> filteringPromotionByType(String type,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);

    long countFliteringPromotion(String type);

    List<PromotionDTO> filteringPromotionByStatus(String status,
                                                  @Param("offset") int offset,
                                                  @Param("limit") int limit);

    long countFliterPromotionByStatus(String status);

    List<PromotionWithContractDTO> useContractPromotion(Integer segment);

    PromotionDTO selectEachPromotion(String proCode);
}
