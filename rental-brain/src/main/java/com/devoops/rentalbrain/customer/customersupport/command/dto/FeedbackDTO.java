package com.devoops.rentalbrain.customer.customersupport.command.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FeedbackDTO {
    private String title;
    private String content;
    private Integer star;
    private String action;
    private Long cumId;
    private Long categoryId;
    private Long empId;
    private Long channelId;
}
