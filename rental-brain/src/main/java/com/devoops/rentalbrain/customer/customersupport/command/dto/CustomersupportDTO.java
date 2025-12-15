package com.devoops.rentalbrain.customer.customersupport.command.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomersupportDTO {
    private String title;
    private String content;
    private String status;
    private String action;
    private Long cumId;
    private Long empId;
    private Long categoryId;
    private Long channelId;
}