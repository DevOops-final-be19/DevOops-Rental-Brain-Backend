package com.devoops.rentalbrain.customer.common;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDto {
    private Long id;
    private String customerCode;
    private String name;
    private String inCharge;
    private String dept;
    private String callNum;
    private String phone;
    private String email;
    private String businessNum;
    private String addr;
    private LocalDateTime lastTransaction;
    private LocalDateTime firstContractDate;
    private String memo;
    private Integer star;
    private String isDeleted;

    // Join된 정보
    private String channelName;
    private String segmentName;
}