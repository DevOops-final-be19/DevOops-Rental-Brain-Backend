package com.devoops.rentalbrain.customer.customersegmenthistory.command.service;

import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandCreateDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandResponseDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandUpdateDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.entity.HistoryCommandEntity;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.repository.HistoryCommandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HistoryCommandServiceImpl implements HistoryCommandService {

    private final HistoryCommandRepository historyCommandRepository;

    // insert -> 수동 저장할 때 (거의 사용 안하고 자동으로 트리거로 사용하는 게 좋을 듯)
    @Override
    public HistoryCommandResponseDTO create(HistoryCommandCreateDTO historyCommandCreateDTO) {

        // DTO -> Entity 변환
        HistoryCommandEntity entity = new HistoryCommandEntity();
        entity.setCustomerId(historyCommandCreateDTO.getCustomerId());
        entity.setPreviousSegmentId(historyCommandCreateDTO.getPreviousSegmentId());
        entity.setCurrentSegmentId(historyCommandCreateDTO.getCurrentSegmentId());
        entity.setReason(historyCommandCreateDTO.getReason());
        entity.setTriggerType(historyCommandCreateDTO.getTriggerType());
        entity.setReferenceType(historyCommandCreateDTO.getReferenceType());
        entity.setReferenceId(historyCommandCreateDTO.getReferenceId());

        HistoryCommandEntity saved = historyCommandRepository.save(entity);

        return new HistoryCommandResponseDTO(saved.getId(), saved.getChangedAt());
    }

    // update
    @Override
    public HistoryCommandResponseDTO update(Long historyId, HistoryCommandUpdateDTO historyCommandUpdateDTO) {
        HistoryCommandEntity entity = historyCommandRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 이력이 없습니다.: " + historyId));

        // 검증
        if (historyCommandUpdateDTO.getReason() != null)
            entity.setReason(historyCommandUpdateDTO.getReason());
        if (historyCommandUpdateDTO.getTriggerType() != null)
            entity.setTriggerType(historyCommandUpdateDTO.getTriggerType());
        if (historyCommandUpdateDTO.getReferenceType() != null)
            entity.setReferenceType(historyCommandUpdateDTO.getReferenceType());
        if (historyCommandUpdateDTO.getReferenceId() != null)
            entity.setReferenceId(historyCommandUpdateDTO.getReferenceId());

        HistoryCommandEntity saved = historyCommandRepository.save(entity);
        return new HistoryCommandResponseDTO(saved.getId(), saved.getChangedAt());
    }

    // delete
    @Override
    public void delete(Long historyId) {
        if (!historyCommandRepository.existsById(historyId)) {
            throw new IllegalArgumentException("삭제할 이력이 없습니다.: " + historyId);
        }
        historyCommandRepository.deleteById(historyId);
    }
}
