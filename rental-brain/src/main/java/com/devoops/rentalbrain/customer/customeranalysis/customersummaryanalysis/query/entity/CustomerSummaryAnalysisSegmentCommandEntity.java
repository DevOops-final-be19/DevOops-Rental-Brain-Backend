package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "segment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerSummaryAnalysisSegmentCommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long segmentId;

    @Column(name = "name", nullable = false, unique = true)
    private String segmentName;

    @Column(name = "content")
    private String segmentContent;

    @Column(name = "total_charge")
    private Long segmentTotalCharge;

    @Column(name = "segment_period")
    private Integer segmentPeriod;

    @Column(name = "is_contracted")
    private Boolean segmentIsContracted;

    @Column(name = "overdued")
    private Integer segmentOverdued;




}
