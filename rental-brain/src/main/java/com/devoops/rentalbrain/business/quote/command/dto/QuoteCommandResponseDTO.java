package com.devoops.rentalbrain.business.quote.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuoteCommandResponseDTO {
    // quote 테이블
    private Long quoteId;

    private LocalDateTime quoteCounselingDate;

    private String quoteCounselor;
    private String quoteSummary;
    private String quoteContent;
    private Integer quoteProcessingTime;
    private Long quoteChannelId;
    private Long quoteCumId;

    // customer join  할 부분
    private String customerName;
    private String customerInCharge;
    private String customerCallNum;
    private String customerEmail;
    private String customerAddr;


    // channel join 할 부분
    private String channelName;
}
