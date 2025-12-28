package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class QuoteInsightRowDTO {
    private Long quoteId;
    private LocalDateTime counselingDate;

    private String quoteSummary;
    private String quoteContent;

    private Long customerId;
    private String customerName;
    private String channelName;

    // 1=성공, 0=실패
    private Integer isSuccess;
}
