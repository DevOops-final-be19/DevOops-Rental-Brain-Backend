package com.devoops.rentalbrain.customer.customerlist.query.dto;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryQueryDTO;

// ▼ [선택된 DTO Import]
import com.devoops.rentalbrain.business.contract.query.dto.AllContractDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDetailDTO;

import com.devoops.rentalbrain.customer.customersupport.query.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteQueryResponseDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDetailResponseDTO extends CustomerDTO {

    // 1. [종합 정보] 탭용: 통합 히스토리 (문의+견적+계약+AS+피드백 합본)
    private List<CustomerHistoryDTO> historyList;

    // 2. [세그먼트 변경 이력] 탭용: 세그먼트 변경 전용 로그
    private List<HistoryQueryDTO> segmentHistoryList;

    // 3. 각 탭별 상세 리스트
    private List<CustomersupportDTO> supportList;       // 문의
    private List<QuoteQueryResponseDTO> quoteList;      // 견적
    private List<AllContractDTO> contractList;      // 계약
    private List<AfterServiceDetailDTO> asList;        // AS
    private List<FeedbackDTO> feedbackList;             // 피드백
    private List<CouponDTO> couponList;                 // 쿠폰
    private List<PromotionDTO> promotionList;           // 프로모션
}