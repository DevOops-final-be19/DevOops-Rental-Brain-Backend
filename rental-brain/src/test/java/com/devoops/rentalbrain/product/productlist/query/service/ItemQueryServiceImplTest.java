package com.devoops.rentalbrain.product.productlist.query.service;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback
class ItemQueryServiceImplTest {
    private final ItemQueryService itemQueryService;

    @Autowired
    public ItemQueryServiceImplTest(ItemQueryService itemQueryService) {
        this.itemQueryService = itemQueryService;
    }

    @MockitoBean
    private OpenAIClient openAIClient;

    @MockitoBean
    private AiCommandService aiCommandService;

    @DisplayName("제품 상세 목록 조회 테스트")
    @Test
    void readAllItemsTest() {
        Assertions.assertDoesNotThrow(() -> {
            List<EachItemDTO> itemList = itemQueryService.readAllItems("L3 네트워크 스위치");
            itemList.forEach(System.out::println);
        });
    }
}