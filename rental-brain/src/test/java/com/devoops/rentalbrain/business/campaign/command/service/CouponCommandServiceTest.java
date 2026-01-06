package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertCouponDTO;
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


@SpringBootTest
@Transactional
@Rollback
class CouponCommandServiceTest {
    private final CouponCommandService couponCommandService;

    @Autowired
    public CouponCommandServiceTest(CouponCommandService couponCommandService) {
        this.couponCommandService = couponCommandService;
    }

    @MockitoBean
    private OpenAIClient openAIClient;

    @MockitoBean
    private AiCommandService aiCommandService;

    @DisplayName("쿠폰 등록 테스트")
    @Test
    void createCouponTest() {
        Assertions.assertDoesNotThrow(() -> {
            InsertCouponDTO coupon = new InsertCouponDTO();
            coupon.setName("예시 쿠폰");
            coupon.setRate(10);
            coupon.setContent("예시 쿠폰 내용");
            coupon.setMinFee(10);
            coupon.setType("R");
            coupon.setMaxNum(10);
            coupon.setDatePeriod(30);
            coupon.setSegmentName("잠재 고객");
            couponCommandService.insertCoupon(coupon);
        });
    }
}