package com.devoops.rentalbrain.customer.customerlist.command.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerlistCommandDTO {
    private String name;
    private String inCharge;
    private String dept;
    private String callNum;
    private String phone;
    private String email;
    private String businessNum;
    private String addr;
    private String memo;
    private Integer star;
    private Long channelId;
    private Long segmentId;
}