package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Arrays.asList;

@SpringBootTest
@Transactional
@Rollback
class CustomerlistQueryServiceImplTest {
    private final CustomerlistQueryService customerlistQueryService;

    @Autowired
    public CustomerlistQueryServiceImplTest(CustomerlistQueryService customerlistQueryService) {
        this.customerlistQueryService = customerlistQueryService;
    }

    @MockitoBean
    private OpenAIClient openAIClient;  // OpenAiConfig 빈 Mock

    @MockitoBean
    private AiCommandService aiCommandService;

    @DisplayName("고객목록 조회 테스트")
    @Test
    void getCustomerList() {
        Assertions.assertDoesNotThrow(() -> {
            CustomerlistSearchDTO criteria = new CustomerlistSearchDTO(1, 10);
            criteria.setName("name");
            criteria.setEmail("email");
            criteria.setSegments(asList("VIP 고객", "잠재 고객"));
            criteria.setStatus("A");
            PageResponseDTO<CustomerDTO> customerList =
                    customerlistQueryService.getCustomerListWithPaging(criteria);
            Assertions.assertNotNull(customerList);
        });
    }
}