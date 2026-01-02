package com.devoops.rentalbrain.customer.channel.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChannelTotalSumDTO {
    private Long customerCount;
    private Long quoteCount;
    private Long feedbackCount;
    private Long customerSupportCount;
    private Long totalCount;
}
