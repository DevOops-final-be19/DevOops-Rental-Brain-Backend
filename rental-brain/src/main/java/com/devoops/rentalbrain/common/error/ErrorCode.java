package com.devoops.rentalbrain.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/* 설명. 에러 코드 정의 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* Common */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C003", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "C004", "권한이 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "대상을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C006", "지원하지 않는 HTTP 메서드입니다."),

    /* Employee */
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "직원을 찾을 수 없습니다."),

    /* Customer List (고객 목록) */
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "CUST001", "고객 정보를 찾을 수 없습니다."),
    CUSTOMER_DUPLICATE(HttpStatus.CONFLICT, "CUST002", "이미 존재하는 고객입니다."),

    /* Customer Support (고객 응대 - 문의, 피드백, 설문) */
    SUPPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "CS001", "문의 내역을 찾을 수 없습니다."),
    FEEDBACK_NOT_FOUND(HttpStatus.NOT_FOUND, "CS002", "피드백 내역을 찾을 수 없습니다."),
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "CS003", "설문 내역을 찾을 수 없습니다."),

    /* After Service */
    AS_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "AS001", "AS/정기점검 요청 값이 올바르지 않습니다."),
    AS_DUPLICATE_SCHEDULE(HttpStatus.CONFLICT, "AS002", "이미 동일 자산·동일 예정일의 점검 일정이 존재합니다."),
    AS_NOT_FOUND(HttpStatus.NOT_FOUND, "AS003", "AS/정기점검 정보를 찾을 수 없습니다."),

    /* Contract */
    CONTRACT_ITEM_STOCK_NOT_ENOUGH(HttpStatus.BAD_REQUEST,"CON001","대여 가능한 상품 수량이 부족합니다."),
    CONTRACT_INVALID_APPROVAL_REQUEST(HttpStatus.BAD_REQUEST,"CON002","결재 요청자를 지정할 수 없습니다."),
    INVALID_CONTRACT_PERIOD(HttpStatus.BAD_REQUEST,"CON003","계약 시작일 또는 계약 기간(개월 수)이 유효하지 않습니다." ),
    CONTRACT_DUPLICATE_ITEM(HttpStatus.BAD_REQUEST,"CON004" , "계약 상품이 중복되었습니다" ),
    CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND,"CON005","계약 정보를 찾을 수 없습니다."),
    INVALID_CONTRACT_STATUS(HttpStatus.BAD_REQUEST,"CON006","현재 계약 상태에서는 해당 작업을 수행할 수 없습니다."),

    /* Approval */
    APPROVAL_MAPPING_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,"APR001","승인 매핑 정보를 찾을 수 없습니다."),
    APPROVAL_ALREADY_PROCESSED(HttpStatus.CONFLICT, "APR002", "이미 처리된 승인 요청입니다."),
    APPROVAL_ACCESS_DENIED(HttpStatus.FORBIDDEN,"APR003" ,"결재권한이 없습니다." ),
    APPROVAL_PREVIOUS_STEP_NOT_COMPLETED(HttpStatus.CONFLICT,"APR004" ,"이전 승인 단계가 완료되지 않았습니다." );

    private final HttpStatus status;
    private final String code;
    private final String message;
}