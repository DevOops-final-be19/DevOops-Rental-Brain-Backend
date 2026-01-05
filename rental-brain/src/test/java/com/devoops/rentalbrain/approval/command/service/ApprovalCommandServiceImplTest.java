package com.devoops.rentalbrain.approval.command.service;

import com.devoops.rentalbrain.approval.command.Repository.ApprovalMappingCommandRepository;
import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import com.devoops.rentalbrain.approval.command.entity.ApprovalMappingCommandEntity;
import com.devoops.rentalbrain.business.campaign.command.service.CouponCommandService;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.PaymentDetailCommandRepository;
import com.devoops.rentalbrain.common.error.ErrorCode;
import com.devoops.rentalbrain.common.error.exception.BusinessException;
import com.devoops.rentalbrain.common.notice.application.facade.NotificationPublisher;
import com.devoops.rentalbrain.common.notice.application.strategy.event.ContractApprovedEvent;
import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.devoops.rentalbrain.employee.command.entity.Employee;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalCommandServiceImplTest {

    @InjectMocks
    private ApprovalCommandServiceImpl approvalCommandService;

    @Mock
    private ApprovalMappingCommandRepository approvalMappingCommandRepository;

    @Mock
    private PaymentDetailCommandRepository paymentDetailCommandRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CouponCommandService couponCommandService;

    @Mock
    private NotificationPublisher notificationPublisher;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void approve_success_finalStep() {
        // given
        Long empId = 1L;
        Long approvalId = 10L;
        Long mappingId = 100L;

        // 로그인 사용자
        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(Long.valueOf(empId.toString()));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        // 결재 담당자 (로그인 사용자와 동일 ID)
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(empId);

        ContractCommandEntity contract = new ContractCommandEntity();
        contract.setId(99L);
        contract.setStartDate(LocalDateTime.now());
        contract.setContractPeriod(3);
        contract.setStatus("P");
        contract.setCurrentStep(1);

        ApprovalCommandEntity approval = ApprovalCommandEntity.builder()
                .id(approvalId)
                .contract(contract)
                .employee(employee)
                .build();

        ApprovalMappingCommandEntity mapping = ApprovalMappingCommandEntity.builder()
                .id(mappingId)
                .approval(approval)
                .employee(employee)
                .step(2)
                .isApproved("U")
                .build();

        when(approvalMappingCommandRepository.findById(mappingId))
                .thenReturn(Optional.of(mapping));

        when(approvalMappingCommandRepository
                .existsByApproval_IdAndStepLessThanAndIsApprovedNot(
                        approvalId, 2, "Y"))
                .thenReturn(false);

        when(approvalMappingCommandRepository
                .existsByApproval_IdAndIsApprovedNot(approvalId, "Y"))
                .thenReturn(false);

        // when
        approvalCommandService.approve(mappingId);

        // then
        assertEquals("Y", mapping.getIsApproved());
        assertEquals("A", approval.getStatus());
        assertEquals("P", contract.getStatus());

        verify(paymentDetailCommandRepository).saveAll(anyList());
        verify(couponCommandService).updateIssuedCoupon(contract.getId());
        verify(notificationPublisher)
                .publish(any(ContractApprovedEvent.class));
    }

    @Test
    void approve_success_notFinalStep() {
        // given
        Long empId = 1L;
        Long approvalId = 10L;
        Long mappingId = 100L;

        // 로그인 사용자
        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(Long.valueOf(empId.toString()));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        // 결재자
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(empId);

        ContractCommandEntity contract = new ContractCommandEntity();
        contract.setId(99L);
        contract.setStatus("W");
        contract.setCurrentStep(1);

        ApprovalCommandEntity approval = ApprovalCommandEntity.builder()
                .id(approvalId)
                .contract(contract)
                .employee(employee)
                .build();

        ApprovalMappingCommandEntity mapping = ApprovalMappingCommandEntity.builder()
                .id(mappingId)
                .approval(approval)
                .employee(employee)
                .step(2)
                .isApproved("U")
                .build();

        when(approvalMappingCommandRepository.findById(mappingId))
                .thenReturn(Optional.of(mapping));

        // 이전 단계 전부 승인
        when(approvalMappingCommandRepository
                .existsByApproval_IdAndStepLessThanAndIsApprovedNot(
                        approvalId, 2, "Y"))
                .thenReturn(false);

        // 아직 미승인 mapping 존재 → 최종 아님
        when(approvalMappingCommandRepository
                .existsByApproval_IdAndIsApprovedNot(approvalId, "Y"))
                .thenReturn(true);

        // when
        approvalCommandService.approve(mappingId);

        // then
        assertEquals("Y", mapping.getIsApproved());
        assertEquals(2, contract.getCurrentStep());

        verify(paymentDetailCommandRepository, never()).saveAll(any());
        verify(couponCommandService, never()).updateIssuedCoupon(any());
        verify(notificationPublisher, never()).publish(any());
    }

    @Test
    void reject_success() {
        // given
        Long empId = 1L;
        Long mappingId = 200L;

        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(Long.valueOf(empId.toString()));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(empId);

        ContractCommandEntity contract = new ContractCommandEntity();
        contract.setId(50L);

        ApprovalCommandEntity approval = ApprovalCommandEntity.builder()
                .contract(contract)
                .employee(employee)
                .build();

        ApprovalMappingCommandEntity mapping = ApprovalMappingCommandEntity.builder()
                .id(mappingId)
                .approval(approval)
                .employee(employee)
                .isApproved("U")
                .build();

        when(approvalMappingCommandRepository.findById(mappingId))
                .thenReturn(Optional.of(mapping));

        when(itemRepository.rollbackItemsToPending(contract.getId()))
                .thenReturn(2);

        // when
        approvalCommandService.reject(mappingId, "사유");

        // then
        assertEquals("N", mapping.getIsApproved());
        assertEquals("R", approval.getStatus());
        assertEquals("R", contract.getStatus());

        verify(itemRepository).rollbackItemsToPending(contract.getId());
        verify(notificationPublisher)
                .publish(any(ContractApprovedEvent.class));
    }

    @Test
    void approve_fail_notMyApproval() {
        // given
        Long loginEmpId = 1L;
        Long approverEmpId = 2L;

        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(Long.valueOf(loginEmpId.toString()));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        Employee approver = mock(Employee.class);
        when(approver.getId()).thenReturn(approverEmpId);

        ApprovalMappingCommandEntity mapping = ApprovalMappingCommandEntity.builder()
                .id(1L)
                .employee(approver)
                .isApproved("U")
                .build();

        when(approvalMappingCommandRepository.findById(1L))
                .thenReturn(Optional.of(mapping));

        // when & then
        BusinessException exception =
                assertThrows(BusinessException.class,
                        () -> approvalCommandService.approve(1L));

        assertEquals(
                ErrorCode.APPROVAL_ACCESS_DENIED,
                exception.getErrorCode()
        );
    }

}

