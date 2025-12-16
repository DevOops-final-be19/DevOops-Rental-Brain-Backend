package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.entity.Promotion;
import com.devoops.rentalbrain.business.campaign.command.repository.PromotionRepository;
import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.segment.command.entity.SegmentCommandEntity;
import com.devoops.rentalbrain.customer.segment.command.repository.SegmentCommandRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromotionCommandServiceImpl implements PromotionCommandService {
    private final ModelMapper modelMapper;
    private final PromotionRepository promotionRepository;
    private final CodeGenerator codeGenerator;
    private final SegmentCommandRepository segmentCommandRepository;

    @Autowired
    public PromotionCommandServiceImpl(ModelMapper modelMapper, PromotionRepository promotionRepository, CodeGenerator codeGenerator, SegmentCommandRepository segmentCommandRepository) {
        this.modelMapper = modelMapper;
        this.promotionRepository = promotionRepository;
        this.codeGenerator = codeGenerator;
        this.segmentCommandRepository = segmentCommandRepository;
    }

    @Override
    @Transactional
    public String insertPromotion(InsertPromotionDTO promotionDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        String segmentName = promotionDTO.getSegmentName();
        SegmentCommandEntity segment = segmentCommandRepository.findAllBySegmentName(segmentName);

        Long segmentId = segment.getSegmentId();
        Promotion promotion = modelMapper.map(promotionDTO, Promotion.class);

        String promotionCode = codeGenerator.generate(CodeType.PROMOTION);
        promotion.setPromotionCode(promotionCode);
        promotion.setSegmentId(segmentId);

        promotionRepository.save(promotion);
        return "promotion insert success";
    }

    @Override
    @Transactional
    public String updatePromotion(Long promotionId, ModifyPromotionDTO promotionDTO) {
        Promotion promotion = promotionRepository.findById(promotionId).get();
        if(promotionDTO.getName() != null && !promotion.getName().equals(promotionDTO.getName())) {
            promotion.setName(promotionDTO.getName());
        }
        if(promotionDTO.getStartDate() != null && !promotion.getStartDate().equals(promotionDTO.getStartDate())) {
            promotion.setStartDate(promotionDTO.getStartDate());
        }
        if(promotionDTO.getEndDate() != null && !promotion.getEndDate().equals(promotionDTO.getEndDate())) {
            promotion.setEndDate(promotionDTO.getEndDate());
        }
        if(promotionDTO.getStatus() != null && !promotion.getStatus().equals(promotionDTO.getStatus())) {
            promotion.setStatus(promotionDTO.getStatus());
        }
        if(promotionDTO.getType() != null && !promotion.getType().equals(promotionDTO.getType())) {
            promotion.setType(promotionDTO.getType());
        }
        if(promotionDTO.getTriggerVal() != null && !promotion.getTriggerVal().equals(promotionDTO.getTriggerVal())) {
            promotion.setTriggerVal(promotionDTO.getTriggerVal());
        }
        if(promotionDTO.getContent() != null && !promotion.getContent().equals(promotionDTO.getContent())) {
            promotion.setContent(promotionDTO.getContent());
        }

        if(promotionDTO.getSegmentName() != null) {
            String segmentName = promotionDTO.getSegmentName();
            SegmentCommandEntity segment = segmentCommandRepository.findAllBySegmentName(segmentName);

            Long segmentId = segment.getSegmentId();
            if(!promotion.getSegmentId().equals(segmentId)) {
                promotion.setSegmentId(segmentId);
            }
        }
        promotionRepository.save(promotion);
        return "promotion update success";
    }

    @Override
    @Transactional
    public String deletePromotion(Long promotionId) {
        promotionRepository.deleteById(promotionId);

        return "promotion delete success";
    }


}
