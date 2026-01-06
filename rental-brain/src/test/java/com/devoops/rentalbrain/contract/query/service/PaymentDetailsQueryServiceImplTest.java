package com.devoops.rentalbrain.contract.query.service;

import com.devoops.rentalbrain.business.contract.query.dto.PaymentOverdueCandidateDTO;
import com.devoops.rentalbrain.business.contract.query.mapper.PaymentDetailsQueryMapper;
import com.devoops.rentalbrain.business.contract.query.service.PaymentDetailsQueryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentDetailsQueryServiceImplTest {

    @Mock
    private PaymentDetailsQueryMapper mapper;

    @InjectMocks
    private PaymentDetailsQueryServiceImpl paymentDetailsQueryService;

    @Test
    @DisplayName("연체 후보 결제 목록 조회 성공")
    void findOverdueCandidates_success() {
        // given
        PaymentOverdueCandidateDTO dto1 = new PaymentOverdueCandidateDTO();
        PaymentOverdueCandidateDTO dto2 = new PaymentOverdueCandidateDTO();

        List<PaymentOverdueCandidateDTO> mockResult = List.of(dto1, dto2);

        when(mapper.findOverdueCandidates())
                .thenReturn(mockResult);

        // when
        List<PaymentOverdueCandidateDTO> result =
                paymentDetailsQueryService.findOverdueCandidates();

        // then
        assertEquals(2, result.size());
        assertEquals(mockResult, result);

        verify(mapper, times(1))
                .findOverdueCandidates();
        verifyNoMoreInteractions(mapper);
    }
}
