package com.devoops.rentalbrain.business.quote.command.dto;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    // 채번 생성기
    private String quoteCode;

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
