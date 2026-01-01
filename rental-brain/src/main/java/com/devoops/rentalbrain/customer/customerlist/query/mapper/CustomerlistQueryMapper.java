package com.devoops.rentalbrain.customer.customerlist.query.mapper;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerHistoryDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerKpiDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;

// 타 도메인 DTO Import (Inner Class 대체)
import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryQueryDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteQueryResponseDTO;
import com.devoops.rentalbrain.business.contract.query.dto.AllContractDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDetailDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.CouponDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerlistQueryMapper {

    // 1. 목록 조회
    List<CustomerDTO> selectCustomerList(CustomerlistSearchDTO criteria);
    long selectCustomerCount(CustomerlistSearchDTO criteria);

    // 2. KPI 조회 (신규 추가)
    CustomerKpiDTO selectCustomerKpi();

    // 3. 상세 조회 (기본 정보)
    // Service에서 DetailDTO로 변환하던 것을 쿼리에서 바로 매핑하거나,
    // 기존처럼 CustomerDto를 받고 Service에서 변환할 수도 있지만,
    // 앞선 XML 설정(id="selectCustomerDetail")에 맞춰 DetailDTO 반환으로 정의합니다.
    CustomerDetailResponseDTO selectCustomerDetail(@Param("id") Long id);

    // 4. 통합 히스토리 조회 (신규 추가)
    List<CustomerHistoryDTO> selectCustomerHistory(@Param("customerId") Long customerId,
                                                   @Param("offset") int offset,
                                                   @Param("amount") int amount);
    // 5. 탭별 상세 내역 조회 (반환 타입 변경 및 메서드명 XML id와 일치)

    // 문의 내역
    List<CustomersupportDTO> selectSupportList(@Param("customerId") Long customerId);

    // 피드백 내역
    List<FeedbackDTO> selectFeedbackList(@Param("customerId") Long customerId);

    // 견적 내역 (Inner Class -> QuoteQueryResponseDTO)
    List<QuoteQueryResponseDTO> selectQuoteList(@Param("customerId") Long customerId);

    // 계약 내역 (Inner Class -> ContractSummaryDTO)
    List<AllContractDTO> selectContractList(@Param("customerId") Long customerId);

    // AS/정기점검 내역 (Inner Class -> AfterServiceSummaryDTO)
    List<AfterServiceDetailDTO> selectAsList(@Param("customerId") Long customerId);

    // 쿠폰 내역 (Inner Class -> CouponDTO)
    List<CouponDTO> selectCouponList(@Param("customerId") Long customerId);

    // 프로모션 내역 (Inner Class -> PromotionDTO)
    List<PromotionDTO> selectPromotionList(@Param("customerId") Long customerId);

    // 세그먼트 변경 내역
    List<HistoryQueryDTO> selectSegmentHistory(@Param("customerId")Long customerId);

}