package com.devoops.rentalbrain.employee.query.service;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class EmployeeQueryServiceImplTest {
    private final EmployeeQueryService employeeQueryService;

    @Autowired
    public EmployeeQueryServiceImplTest(EmployeeQueryService employeeQueryService) {
        this.employeeQueryService = employeeQueryService;
    }

    @MockitoBean
    private OpenAIClient openAIClient;

    @MockitoBean
    private AiCommandService aiCommandService;

    @DisplayName("직원 목록 조회 테스트")
    @Test
    void getEmpListTest() {
        Assertions.assertDoesNotThrow(() -> employeeQueryService.getEmpList());
    }
}