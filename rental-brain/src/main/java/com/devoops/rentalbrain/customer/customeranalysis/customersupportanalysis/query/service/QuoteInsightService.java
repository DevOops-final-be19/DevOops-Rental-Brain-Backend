package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.service;

import java.time.LocalDate;

public interface QuoteInsightService {

    /**
     * 기간 내 견적/상담 데이터를 기반으로 AI 인사이트 JSON을 생성
     * @param start      시작일(포함)
     * @param end        종료일(포함)
     * @param windowDays 성공 판정 윈도우(견적일 이후 N일 내 계약 시작일 존재 시 성공)
     * @param sampleEach 성공/실패 각각 샘플링할 최대 건수
     * @return AI가 생성한 JSON 문자열(유효 JSON)
     */
    String analyzeQuoteInsight(LocalDate start, LocalDate end, int windowDays, int sampleEach);

}
