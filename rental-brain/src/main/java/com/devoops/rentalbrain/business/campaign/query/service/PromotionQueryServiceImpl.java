package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponWithContractDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionWithContractDTO;
import com.devoops.rentalbrain.business.campaign.query.mapper.PromotionMapper;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionQueryServiceImpl implements PromotionQueryService {
    private final PromotionMapper promotionMapper;

    @Autowired
    public PromotionQueryServiceImpl(PromotionMapper promotionMapper) {
        this.promotionMapper = promotionMapper;
    }


    @Override
    public PageResponseDTO<PromotionDTO> readPromotionList(Criteria criteria) {
        List<PromotionDTO> promotionList = promotionMapper.selectPromotion(criteria.getOffset(),
                criteria.getAmount());
        long totalCount = promotionMapper.countPromotionList();

        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(promotionList, totalCount, paging);
    }

    @Override
    public PageResponseDTO<PromotionDTO> searchPromotion(String keyword, Criteria criteria) {
        List<PromotionDTO> promotionList = promotionMapper.searchPromotion(keyword, criteria.getOffset(),
                criteria.getAmount());
        long totalCount = promotionMapper.countSearchedPromotion(keyword);
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);
        return new PageResponseDTO<>(promotionList, totalCount, paging);
    }

    @Override
    public PageResponseDTO<PromotionDTO> filterPromotionByType(String type, Criteria criteria) {
        List<PromotionDTO> promotionList = promotionMapper.filteringPromotionByType(type, criteria.getOffset(),
                criteria.getAmount());
        long totalCount = promotionMapper.countFliteringPromotion(type);
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);
        return new PageResponseDTO<>(promotionList, totalCount, paging);
    }

    @Override
    public PageResponseDTO<PromotionDTO> filterPromotionByStatus(String status, Criteria criteria) {
        List<PromotionDTO> promotionList = promotionMapper.filteringPromotionByStatus(status, criteria.getOffset(),
                criteria.getAmount());
        long totalCount = promotionMapper.countFliterPromotionByStatus(status);
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);
        return new PageResponseDTO<>(promotionList, totalCount, paging);
    }

    @Override
    public List<PromotionWithContractDTO> useContractPromotion(Integer segment) {
        List<PromotionWithContractDTO> promotionList = promotionMapper.useContractPromotion(segment);
        return promotionList;
    }

    @Override
    public PromotionDTO readDetailPromotion(String proCode) {
        PromotionDTO promotion = promotionMapper.selectEachPromotion(proCode);
        return promotion;
    }
}
