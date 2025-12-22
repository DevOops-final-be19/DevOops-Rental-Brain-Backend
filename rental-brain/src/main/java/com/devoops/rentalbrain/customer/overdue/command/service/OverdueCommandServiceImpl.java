package com.devoops.rentalbrain.customer.overdue.command.service;

import com.devoops.rentalbrain.customer.overdue.command.dto.ItemOverdueCommandDTO;
import com.devoops.rentalbrain.customer.overdue.command.dto.PayOverdueCommandDTO;
import com.devoops.rentalbrain.customer.overdue.command.entity.PayOverdue;
import com.devoops.rentalbrain.customer.overdue.command.repository.ItemOverdueRepository;
import com.devoops.rentalbrain.customer.overdue.command.repository.PayOverdueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OverdueCommandServiceImpl implements OverdueCommandService {

    private final PayOverdueRepository payRepo;
//    private final ItemOverdueRepository itemRepo;

    @Override
    public void updatePayOverdue(Long overdueId, PayOverdueCommandDTO dto) {

        PayOverdue entity = payRepo.findById(overdueId)
                .orElseThrow(() -> new IllegalArgumentException("수납 연체 정보 없음"));

        if ("C".equals(dto.getStatus())) {
            entity.resolve(dto.getPaidDate());
        } else {
            entity.changeStatus(dto.getStatus());
        }
    }

//    @Override
//    public void updateItemOverdue(Long id, ItemOverdueCommandDTO req) {
//        ItemOverdue overdue = itemRepo.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("연체 없음"));
//
//        overdue.update(req.getStatus());
//    }
}
