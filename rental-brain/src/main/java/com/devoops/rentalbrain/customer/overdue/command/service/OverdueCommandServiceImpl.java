package com.devoops.rentalbrain.customer.overdue.command.service;

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

        // 같은 계약의 제품 연체도 함께 해결
        itemRepo.findByContractIdAndStatus(
                entity.getContractId(), "P").ifPresent(ItemOverdue::resolve);
    }
}
