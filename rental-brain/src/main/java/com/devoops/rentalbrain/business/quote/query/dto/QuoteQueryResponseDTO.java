package com.devoops.rentalbrain.business.quote.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuoteQueryResponseDTO {

    // quote 테이블
    private Long quoteId;

    // 채번 생성기
    private String quoteCode;

    private LocalDateTime quoteCounselingDate;
    private String quoteCounselor;
    private String quoteSummary;
    private Integer quoteProcessingTime;

    // customer join  할 부분
    private String customerName;
    private String customerInCharge;
    private String customerCallNum;

    // channel join 할 부분
    private String channelName;
}