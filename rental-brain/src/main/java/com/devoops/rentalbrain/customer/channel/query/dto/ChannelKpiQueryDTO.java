package com.devoops.rentalbrain.customer.channel.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChannelKpiQueryDTO {

    private Long channelId;
    private String channelName;

    private Long customerCount;
    private Long quoteCount;
    private Long feedbackCount;
    private Long customerSupportCount;
    private Long totalCount;
}
