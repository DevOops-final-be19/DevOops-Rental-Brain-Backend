package com.devoops.rentalbrain.product.maintenance.command.service;

import com.devoops.rentalbrain.product.maintenance.command.dto.AfterServiceCreateRequest;
import com.devoops.rentalbrain.product.maintenance.command.dto.AfterServiceUpdateRequest;
import com.devoops.rentalbrain.product.maintenance.command.entity.AfterService;
import com.devoops.rentalbrain.product.maintenance.command.repository.AfterServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AfterServiceCommandServiceImpl implements AfterServiceCommandService {

    private final AfterServiceRepository repository;

    @Override
    public Long create(AfterServiceCreateRequest req) {
        AfterService entity = AfterService.builder()
                .engineer(req.getEngineer())
                .type(req.getType())
                .dueDate(req.getDueDate())
                .status("P")
                .contents(req.getContents())
                .itemId(req.getItemId())
                .customerId(req.getCustomerId())
                .build();

        return repository.save(entity).getId();
    }

    @Override
    public void update(Long id, AfterServiceUpdateRequest req) {
        AfterService as = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AS not found"));

        if (req.getEngineer() != null) as.setEngineer(req.getEngineer());
        if (req.getDueDate() != null) as.setDueDate(req.getDueDate());
        if (req.getContents() != null) as.setContents(req.getContents());
        if (req.getStatus() != null) as.setStatus(req.getStatus());

        repository.save(as);
    }
}
