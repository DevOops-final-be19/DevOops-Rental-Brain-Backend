package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.entity.Promotion;
import com.devoops.rentalbrain.business.campaign.command.entity.PromotionLog;
import com.devoops.rentalbrain.business.campaign.command.repository.PromotionLogRepository;
import com.devoops.rentalbrain.business.campaign.command.repository.PromotionRepository;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.segment.command.entity.SegmentCommandEntity;
import com.devoops.rentalbrain.customer.segment.command.repository.SegmentCommandRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PromotionCommandServiceImpl implements PromotionCommandService {
    private final ModelMapper modelMapper;
    private final PromotionRepository promotionRepository;
    private final CodeGenerator codeGenerator;
    private final SegmentCommandRepository segmentCommandRepository;
    private final ContractCommandRepository contractCommandRepository;
    private final PromotionLogRepository promotionLogRepository;

    @Autowired
    public PromotionCommandServiceImpl(ModelMapper modelMapper,
                                       PromotionRepository promotionRepository,
                                       CodeGenerator codeGenerator,
                                       SegmentCommandRepository segmentCommandRepository,
                                       ContractCommandRepository contractCommandRepository,
                                       PromotionLogRepository promotionLogRepository) {
        this.modelMapper = modelMapper;
        this.promotionRepository = promotionRepository;
        this.codeGenerator = codeGenerator;
        this.segmentCommandRepository = segmentCommandRepository;
        this.contractCommandRepository = contractCommandRepository;
        this.promotionLogRepository = promotionLogRepository;
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
    public String updatePromotion(String proCode, ModifyPromotionDTO promotionDTO) {
        Promotion promotion = promotionRepository.findByPromotionCode(proCode);
        promotion.setStartDate(null);
        promotion.setEndDate(null);
        promotion.setTriggerVal(null);
        if(promotionDTO.getType() != null && !promotion.getType().equals(promotionDTO.getType())) {
            promotion.setType(promotionDTO.getType());
        }
        if(promotionDTO.getName() != null && !promotion.getName().equals(promotionDTO.getName())) {
            promotion.setName(promotionDTO.getName());
        }
        if ("M".equals(promotion.getType())) {
            LocalDateTime oldStart = promotion.getStartDate();
            LocalDateTime newStart = promotionDTO.getStartDate();

            if (newStart != null && !newStart.equals(oldStart)) {
                promotion.setStartDate(newStart);
            }
            if ("A".equals(promotionDTO.getType()) && newStart == null) {
                promotion.setStartDate(null);
            }

            LocalDateTime oldEnd = promotion.getEndDate();
            LocalDateTime newEnd = promotionDTO.getEndDate();

            if (newEnd != null && !newEnd.equals(oldEnd)) {
                promotion.setEndDate(newEnd);
            }
            if ("A".equals(promotionDTO.getType()) && newEnd == null) {
                promotion.setEndDate(null);
            }
        }

        if(promotionDTO.getStatus() != null && !promotion.getStatus().equals(promotionDTO.getStatus())) {
            promotion.setStatus(promotionDTO.getStatus());
        }
        if ("A".equals(promotion.getType())) {
            String oldTrigger = promotion.getTriggerVal();
            String newTrigger = promotionDTO.getTriggerVal();

            if (newTrigger != null && !newTrigger.equals(oldTrigger)) {
                promotion.setTriggerVal(newTrigger);
            }

            if ("M".equals(promotionDTO.getType()) && newTrigger == null) {
                promotion.setTriggerVal(null);
            }
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
    public String deletePromotion(String proCode) {
        promotionRepository.deleteByPromotionCode(proCode);

        return "promotion delete success";
    }

    @Override
    @Transactional
    public String createPromotionLog(Long promotionId, Long contractId) {
        ContractCommandEntity contract = contractCommandRepository.findById(contractId).get();

        LocalDateTime start = contract.getStartDate();

        PromotionLog promotionLog = new PromotionLog();
        promotionLog.setParticipationDate(start);
        promotionLog.setCumId(contract.getCustomer().getId());
        promotionLog.setPromotionId(promotionId);

        promotionLogRepository.save(promotionLog);
        return "promotion insert success";
    }


}
