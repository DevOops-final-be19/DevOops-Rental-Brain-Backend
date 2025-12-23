package com.devoops.rentalbrain.customer.overdue.command.service;

import com.devoops.rentalbrain.customer.overdue.command.dto.PayOverdueCommandDTO;

public interface OverdueCommandService {
    void updatePayOverdue(Long id, PayOverdueCommandDTO request);
}
