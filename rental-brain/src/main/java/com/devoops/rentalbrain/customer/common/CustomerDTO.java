package com.devoops.rentalbrain.customer.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String customerCode;
    private String name;        // 기업명
    private String inCharge;    // 담당자
    private String dept;        // 부서
    private String callNum;     // 전화번호 (회사)
    private String phone;       // 휴대전화 (담당자)
    private String email;
    private String businessNum;
    private String addr;
    private String memo;
    private String isDeleted;
    private String segmentName; // 세그먼트
    private Integer segmentId;
    private String channelName;

    // ▼ 추가된 필드 (목록 화면용)
    private LocalDate firstContractDate; // 첫 계약일
    private Long totalTransactionAmount; // 총 거래액
    private Integer contractCount;       // 계약 수
}