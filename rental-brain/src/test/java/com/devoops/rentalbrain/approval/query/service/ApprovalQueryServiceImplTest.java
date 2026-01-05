package com.devoops.rentalbrain.approval.query.service;

import com.devoops.rentalbrain.approval.query.dto.ApprovalCompletedDTO;
import com.devoops.rentalbrain.approval.query.dto.ApprovalProgressDTO;
import com.devoops.rentalbrain.approval.query.dto.ApprovalStatusDTO;
import com.devoops.rentalbrain.approval.query.dto.PendingApprovalDTO;
import com.devoops.rentalbrain.approval.query.mapper.ApprovalQueryMapper;
import com.devoops.rentalbrain.common.error.ErrorCode;
import com.devoops.rentalbrain.common.error.exception.BusinessException;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalQueryServiceImplTest {

    @InjectMocks
    private ApprovalQueryServiceImpl approvalQueryService;

    @Mock
    private ApprovalQueryMapper approvalQueryMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void getApprovalStatus_success() {
        // given
        Long empId = 1L;

        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(Long.valueOf(empId.toString()));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        ApprovalStatusDTO dto = new ApprovalStatusDTO();
        when(approvalQueryMapper.getApprovalStatus(empId))
                .thenReturn(dto);

        // when
        ApprovalStatusDTO result = approvalQueryService.getApprovalStatus();

        // then
        assertNotNull(result);
        verify(approvalQueryMapper).getApprovalStatus(empId);
    }

    @Test
    void getPendingApprovals_success() {
        // given
        Long empId = 1L;
        Criteria criteria = new Criteria(1, 10);

        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(Long.valueOf(empId.toString()));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        List<PendingApprovalDTO> list =
                List.of(new PendingApprovalDTO());

        when(approvalQueryMapper
                .selectPendingApprovalsByEmpIdWithPaging(empId, criteria))
                .thenReturn(list);

        when(approvalQueryMapper
                .countPendingApprovalsByEmpId(empId, criteria))
                .thenReturn(1L);

        // when
        PageResponseDTO<PendingApprovalDTO> response =
                approvalQueryService.getPendingApprovals(criteria);

        // then
        assertEquals(1, response.getContents().size());
        assertEquals(1L, response.getTotalCount());

        verify(approvalQueryMapper)
                .selectPendingApprovalsByEmpIdWithPaging(empId, criteria);
        verify(approvalQueryMapper)
                .countPendingApprovalsByEmpId(empId, criteria);
    }

    @Test
    void getApprovalProgress_success() {
        // given
        Long empId = 1L;
        Criteria criteria = new Criteria(1, 10);

        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(Long.valueOf(empId.toString()));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        List<ApprovalProgressDTO> list =
                List.of(new ApprovalProgressDTO());

        when(approvalQueryMapper
                .selectInProgressApprovals(empId, criteria))
                .thenReturn(list);

        when(approvalQueryMapper
                .countInProgressApprovals(empId, criteria))
                .thenReturn(1L);

        // when
        PageResponseDTO<ApprovalProgressDTO> response =
                approvalQueryService.getApprovalProgress(criteria);

        // then
        assertEquals(1, response.getContents().size());
        assertEquals(1L, response.getTotalCount());

        verify(approvalQueryMapper)
                .selectInProgressApprovals(empId, criteria);
        verify(approvalQueryMapper)
                .countInProgressApprovals(empId, criteria);
    }

    @Test
    void getApprovalCompleted_success() {
        // given
        Long empId = 1L;
        Criteria criteria = new Criteria(1, 10);

        UserImpl user = mock(UserImpl.class);
        when(user.getId()).thenReturn(Long.valueOf(empId.toString()));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        List<ApprovalCompletedDTO> list =
                List.of(new ApprovalCompletedDTO());

        when(approvalQueryMapper
                .selectCompletedApprovals(empId, criteria))
                .thenReturn(list);

        when(approvalQueryMapper
                .countCompletedApprovals(empId, criteria))
                .thenReturn(1L);

        // when
        PageResponseDTO<ApprovalCompletedDTO> response =
                approvalQueryService.getApprovalCompleted(criteria);

        // then
        assertEquals(1, response.getContents().size());
        assertEquals(1L, response.getTotalCount());
        assertNotNull(response.getPaging());

        verify(approvalQueryMapper)
                .selectCompletedApprovals(empId, criteria);
        verify(approvalQueryMapper)
                .countCompletedApprovals(empId, criteria);
    }

    @Test
    void getApprovalStatus_fail_unauthorized() {
        when(securityContext.getAuthentication()).thenReturn(null);

        BusinessException exception =
                assertThrows(BusinessException.class,
                        () -> approvalQueryService.getApprovalStatus());

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
    }

    @Test
    void getApprovalStatus_fail_invalidPrincipal() {
        // given
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymous");

        // when & then
        BusinessException exception =
                assertThrows(BusinessException.class,
                        () -> approvalQueryService.getApprovalStatus());

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
    }


}
