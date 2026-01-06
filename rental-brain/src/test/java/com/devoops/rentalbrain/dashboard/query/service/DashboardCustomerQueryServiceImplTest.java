package com.devoops.rentalbrain.dashboard.query.service;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.devoops.rentalbrain.dashboard.query.dto.QuarterlyCustomerTrendResponseDTO;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
class DashboardCustomerQueryServiceImplTest {
    private final DashboardCustomerQueryService dashboardCustomerQueryService;

    @Autowired
    public DashboardCustomerQueryServiceImplTest(DashboardCustomerQueryService dashboardCustomerQueryService) {
        this.dashboardCustomerQueryService = dashboardCustomerQueryService;
    }

    @MockitoBean
    private OpenAIClient openAIClient;

    @MockitoBean
    private AiCommandService aiCommandService;

    @DisplayName("분기별 고객 수 조회 테스트")
    @Test
    void quarterlyCustomerTrendTest() {
        Assertions.assertDoesNotThrow(() -> {
            QuarterlyCustomerTrendResponseDTO quarterlyCustomerTrendResponseDTO =
                    dashboardCustomerQueryService.getQuarterlyTrend(2025);
            Assertions.assertNotNull(quarterlyCustomerTrendResponseDTO);
        });
    }
}