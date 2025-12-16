package com.devoops.rentalbrain.customer.customerlist.query.dto;

import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDetailResponseDTO extends CustomerDTO {

    // 1. 문의 내역
    private List<CustomersupportDTO> supportList;

    // 2. 피드백 내역
    private List<FeedbackDTO> feedbackList;

    // 3. 견적 내역
    private List<CustomerQuoteDTO> quoteList;

    // 4. 계약 내역
    private List<CustomerContractDTO> contractList;

    // 5. AS 내역
    private List<CustomerAsDTO> asList;

    // 6. 쿠폰 보유/사용 내역
    private List<CustomerCouponDTO> couponList;

    // 7. 프로모션 참여 내역
    private List<CustomerPromotionDTO> promotionList;

    // (고객 메모는 부모 클래스인 CustomerDto의 memo 필드 사용)

    // --- Inner DTOs for simple history view ---

    @Data
    public static class CustomerQuoteDTO {
        private Long id;
        private String quoteCode;
        private LocalDateTime counselingDate;
        private String counselor;
        private String summary;
        private String channelName;
    }

    @Data
    public static class CustomerContractDTO {
        private Long id;
        private String contractCode;
        private String name;
        private String status;     // 진행중, 만료 등
        private Long totalAmount;
        private LocalDateTime startDate;
    }

    @Data
    public static class CustomerAsDTO {
        private Long id;
        private String afterServiceCode;
        private String type;       // 정기점검(R) / AS(A)
        private String status;
        private LocalDateTime dueDate;
        private String itemName;   // 대상 장비
        private String engineer;
    }

    @Data
    public static class CustomerCouponDTO {
        private Long id;           // issued_coupon id
        private String couponName;
        private String isUsed;
        private LocalDateTime issuedDate;
        private LocalDateTime endDate;
    }

    @Data
    public static class CustomerPromotionDTO {
        private Long id;
        private String promotionName;
        private LocalDateTime participationDate;
        private String status; // 프로모션 상태
    }
}