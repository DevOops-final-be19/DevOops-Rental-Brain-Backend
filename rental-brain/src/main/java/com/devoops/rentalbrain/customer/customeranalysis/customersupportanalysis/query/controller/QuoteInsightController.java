package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.controller;


import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.service.QuoteInsightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@Slf4j
@RequestMapping("/insight")
public class QuoteInsightController {

    private final QuoteInsightService quoteInsightService;

    @Autowired
    public QuoteInsightController(QuoteInsightService quoteInsightService) {
        this.quoteInsightService = quoteInsightService;
    }


    // 일단 견적 성공, 실패에대한 요인들을 50 개 씩 샘플링 -> 전체를 긁으면 토큰 비용 문제 생길듯
    @PostMapping("/quoteAnalyze")
    public ResponseEntity<?> analyze(
            @RequestParam String month,
            @RequestParam(defaultValue = "60") int windowDays,
            @RequestParam(defaultValue = "50") int sampleEach    // 몇개 샘플링 해서 검사할지
    ) {
        // 월로만 받을 수 있게
        YearMonth ym = YearMonth.parse(month);  // month는 반드시 "YYYY-MM"
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        log.info("견적 성공 실패요인 analyze start={} end={} windowDays={} sampleEach={}", start, end, windowDays, sampleEach);
        String json = quoteInsightService.analyzeQuoteInsight(start, end, windowDays, sampleEach);
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }
}
