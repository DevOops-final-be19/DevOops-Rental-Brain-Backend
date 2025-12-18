package com.devoops.rentalbrain.customer.customerlist.query.dto;

import com.devoops.rentalbrain.customer.common.CustomerDTO;
// 타 도메인 DTO Import
import com.devoops.rentalbrain.customer.customersupport.query.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteQueryResponseDTO;
import com.devoops.rentalbrain.business.contract.query.dto.ContractSummaryDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceSummaryDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDetailResponseDTO extends CustomerDTO {

    // 통합 히스토리 (상세 화면 전용)
    private List<CustomerHistoryDTO> historyList;

    // 탭별 데이터 리스트 (기존 DTO 활용)
    private List<CustomersupportDTO> supportList;       // 문의
    private List<FeedbackDTO> feedbackList;             // 피드백
    private List<QuoteQueryResponseDTO> quoteList;      // 견적
    private List<ContractSummaryDTO> contractList;      // 계약
    private List<AfterServiceSummaryDTO> asList;        // AS
    private List<CouponDTO> couponList;                 // 쿠폰
    private List<PromotionDTO> promotionList;           // 프로모션
}