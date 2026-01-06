package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
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
class CouponQueryServiceImplTest {
    private final CouponQueryService couponQueryService;

    @Autowired
    public CouponQueryServiceImplTest(CouponQueryService couponQueryService) {
        this.couponQueryService = couponQueryService;
    }

    @MockitoBean
    private OpenAIClient openAIClient;

    @MockitoBean
    private AiCommandService aiCommandService;


    @DisplayName("쿠폰 조회 테스트")
    @Test
    void findAll() {
        Assertions.assertDoesNotThrow(() -> {
            PageResponseDTO<CouponDTO> couponList = couponQueryService.readCouponList(new Criteria());
            Assertions.assertNotNull(couponList);
        });
    }
}