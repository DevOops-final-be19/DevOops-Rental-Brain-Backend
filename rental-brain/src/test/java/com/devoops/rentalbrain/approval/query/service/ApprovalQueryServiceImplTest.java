package com.devoops.rentalbrain.approval.query.service;

import com.devoops.rentalbrain.approval.query.dto.ApprovalStatusDTO;
import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@Rollback
class ApprovalQueryServiceImplTest {
    private final ApprovalQueryService approvalQueryService;

    @Autowired
    public ApprovalQueryServiceImplTest(ApprovalQueryService approvalQueryService) {
        this.approvalQueryService = approvalQueryService;
    }

    @MockitoBean
    private OpenAIClient openAIClient;

    @MockitoBean
    private AiCommandService aiCommandService;

    @DisplayName("견적 상태 조회 테스트")
    @Test
    void getApprovalStatusTest() {
        // UserImpl Mock 생성
        UserImpl mockUser = mock(UserImpl.class);
        when(mockUser.getId()).thenReturn((long)1);
        when(mockUser.getAuthorities()).thenReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN_MANAGE")));

        Authentication auth = new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        ApprovalStatusDTO result = approvalQueryService.getApprovalStatus();
        assertNotNull(result);
    }
}