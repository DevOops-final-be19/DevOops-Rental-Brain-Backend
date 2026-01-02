package com.devoops.rentalbrain.customer.overdue.command.service;

import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueCandidateDTO;
import com.devoops.rentalbrain.customer.overdue.command.entity.ItemOverdue;
import com.devoops.rentalbrain.customer.overdue.command.repository.ItemOverdueRepository;
import com.devoops.rentalbrain.customer.overdue.query.mapper.OverdueQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemOverdueAutoCreateServiceImpl implements ItemOverdueAutoCreateService{

    private final ItemOverdueRepository itemOverdueRepository;
    private final OverdueQueryMapper overdueQueryMapper;

    @Override
    public void createItemOverdueFromExpiredContract() {

        List<ItemOverdueCandidateDTO> candidates =
                overdueQueryMapper.findItemOverdueCandidates();

        for (ItemOverdueCandidateDTO dto : candidates) {

            if (itemOverdueRepository.existsByContractIdAndStatus(dto.getContractId(), "P")) {
                continue;
            }

            ItemOverdue entity = ItemOverdue.create(
                    dto.getContractId(),
                    dto.getCustomerId(),
                    dto.getItemCount(),
                    dto.getDueDate(),
                    dto.getOverduePeriod()
            );

            itemOverdueRepository.save(entity);
        }
    }
}



