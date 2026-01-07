package com.devoops.rentalbrain.customer.overdue.command.service;

import com.devoops.rentalbrain.customer.overdue.command.dto.ItemOverdueCommandDTO;
import com.devoops.rentalbrain.customer.overdue.command.dto.PayOverdueCommandDTO;
import com.devoops.rentalbrain.customer.overdue.command.entity.ItemOverdue;
import com.devoops.rentalbrain.customer.overdue.command.entity.PayOverdue;
import com.devoops.rentalbrain.customer.overdue.command.repository.ItemOverdueRepository;
import com.devoops.rentalbrain.customer.overdue.command.repository.PayOverdueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OverdueCommandServiceImpl implements OverdueCommandService {

    private final PayOverdueRepository payRepo;
    private final ItemOverdueRepository itemRepo;

    @Override
    public void updatePayOverdue(Long overdueId, PayOverdueCommandDTO dto) {

        PayOverdue entity = payRepo.findById(overdueId)
                .orElseThrow(() -> new IllegalArgumentException("수납 연체 정보 없음"));

        LocalDate paidDate = dto.getPaidDate();

        if (paidDate == null) {
            throw new IllegalArgumentException("수납 연체 해결 시 납부일은 필수입니다.");
        }

        if (paidDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("미래 날짜는 선택할 수 없습니다.");
        }

        if ("C".equals(entity.getStatus())) {
            throw new IllegalStateException("이미 해결된 수납 연체입니다.");
        }

        // 수납 연체 해결
        entity.resolve(paidDate.atStartOfDay());

    }

    public void updateItemOverdue(Long overdueId, ItemOverdueCommandDTO dto) {

        ItemOverdue entity = itemRepo.findById(overdueId)
                .orElseThrow(() -> new IllegalArgumentException("제품 연체 정보 없음"));

        if ("C".equals(entity.getStatus())) {
            throw new IllegalStateException("이미 해결된 제품 연체입니다.");
        }

        if (dto.getCount() != null) {
            if (dto.getCount() < 0) {
                throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
            }
            entity.changeCount(dto.getCount());
        }

        // 해결 처리
        if (Boolean.TRUE.equals(dto.getResolved())) {
            entity.resolve();
        }

        // count가 0이면 자동 해결
        if (dto.getCount() != null && dto.getCount() == 0) {
            entity.resolve();
        }
    }

    @Override
    public void deletePayOverdue(Long id) {

        PayOverdue entity = payRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("수납 연체 정보 없음. id=" + id));

        if ("C".equals(entity.getStatus())) {
            throw new IllegalStateException("해결된 수납 연체는 삭제할 수 없습니다.");
        }

        payRepo.delete(entity);
    }

    @Override
    public void deleteItemOverdue(Long id) {

        ItemOverdue entity = itemRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("제품 연체 정보 없음. id=" + id));

        if ("C".equals(entity.getStatus())) {
            throw new IllegalStateException("해결된 제품 연체는 삭제할 수 없습니다.");
        }

        itemRepo.delete(entity);
    }
}
