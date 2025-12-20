package com.devoops.rentalbrain.customer.segment.command.service;

import com.devoops.rentalbrain.customer.segment.command.dto.SegmentCommandCreateDTO;
import com.devoops.rentalbrain.customer.segment.command.dto.SegmentCommandResponseDTO;
import com.devoops.rentalbrain.customer.segment.command.entity.SegmentCommandEntity;
import com.devoops.rentalbrain.customer.segment.command.repository.SegmentCommandRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SegmentCommandServiceImpl implements SegmentCommandService {

    private final SegmentCommandRepository segmentCommandRepository;

    @Autowired
    public SegmentCommandServiceImpl(SegmentCommandRepository segmentCommandRepository) {
        this.segmentCommandRepository = segmentCommandRepository;
    }

    @Override
    @Transactional
    public SegmentCommandCreateDTO insertSegment(SegmentCommandCreateDTO segmentCommandCreateDTO) {

        if (segmentCommandCreateDTO == null) {
            log.info("세그먼트 생성 실패 dto=null");
            throw new IllegalArgumentException("요청 값이 비었습니다.");
        }
        if (segmentCommandCreateDTO.getSegmentName() == null || segmentCommandCreateDTO.getSegmentName().trim().isEmpty()) {
            log.info("세그먼트 생성 실패: segmentName 누락 dto={}", segmentCommandCreateDTO);
            throw new IllegalArgumentException("세그먼트명은 필수입니다.");
        }
        if (segmentCommandCreateDTO.getSegmentContent() == null || segmentCommandCreateDTO.getSegmentContent().trim().isEmpty()) {
            log.info("세그먼트 생성 실패: segmentContent 누락 dto={}", segmentCommandCreateDTO);
            throw new IllegalArgumentException("세그먼트 내용은 필수입니다.");
        }


        SegmentCommandEntity entity = new SegmentCommandEntity();
        entity.setSegmentName(segmentCommandCreateDTO.getSegmentName().trim());
        entity.setSegmentContent(segmentCommandCreateDTO.getSegmentContent().trim());

        if (segmentCommandCreateDTO.getSegmentTotalCharge() != null)
            entity.setSegmentTotalCharge(segmentCommandCreateDTO.getSegmentTotalCharge());

        if (segmentCommandCreateDTO.getSegmentPeriod() != null)
            entity.setSegmentPeriod(segmentCommandCreateDTO.getSegmentPeriod());

        if (segmentCommandCreateDTO.getSegmentIsContracted() != null)
            entity.setSegmentIsContracted(segmentCommandCreateDTO.getSegmentIsContracted());

        if (segmentCommandCreateDTO.getSegmentOverdued() != null)
            entity.setSegmentOverdued(segmentCommandCreateDTO.getSegmentOverdued());
        SegmentCommandEntity saved = segmentCommandRepository.save(entity);

        log.info("세그먼트 생성 완료 segmentId={}, segmentName={}",
                saved.getSegmentId(), saved.getSegmentName());

        return new SegmentCommandCreateDTO(
                saved.getSegmentId(),
                saved.getSegmentName(),
                saved.getSegmentContent()
        );
    }

    // 세그먼트 상태 업데이트시 알림 필요(신규, 일반, VIP, 확장의사 -> 이탈 위험 고객, 블랙리스트
    @Override
    @Transactional
    public SegmentCommandResponseDTO updateSegment(Long segmentId, SegmentCommandResponseDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("요청 값이 비었습니다.");
        }

        SegmentCommandEntity entity = segmentCommandRepository.findById(segmentId)
                .orElseThrow(() -> new IllegalArgumentException("세그먼트가 존재하지 않습니다. id=" + segmentId));

        if (dto.getSegmentName() != null && !dto.getSegmentName().trim().isEmpty()) {
            entity.setSegmentName(dto.getSegmentName().trim());
        }
        if (dto.getSegmentContent() != null && !dto.getSegmentContent().trim().isEmpty()) {
            entity.setSegmentContent(dto.getSegmentContent().trim());
        }

        if (dto.getSegmentTotalCharge() != null) entity.setSegmentTotalCharge(dto.getSegmentTotalCharge());
        if (dto.getSegmentPeriod() != null) entity.setSegmentPeriod(dto.getSegmentPeriod());
        if (dto.getSegmentIsContracted() != null) entity.setSegmentIsContracted(dto.getSegmentIsContracted());
        if (dto.getSegmentOverdued() != null) entity.setSegmentOverdued(dto.getSegmentOverdued());

        SegmentCommandEntity saved = segmentCommandRepository.save(entity);

        return new SegmentCommandResponseDTO(
                saved.getSegmentId(),
                saved.getSegmentName(),
                saved.getSegmentContent(),
                saved.getSegmentTotalCharge(),
                saved.getSegmentPeriod(),
                saved.getSegmentIsContracted(),
                saved.getSegmentOverdued()
        );
    }

    @Override
    @Transactional
    public void deleteSegment(Long segmentId) {
        if (!segmentCommandRepository.existsById(segmentId)) {
            throw new IllegalArgumentException("세그먼트가 존재하지 않습니다. id=" + segmentId);
        }
        segmentCommandRepository.deleteById(segmentId);
        log.info("세그먼트 삭제 완료 id={}", segmentId);
    }
}
