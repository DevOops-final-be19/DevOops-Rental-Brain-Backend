package com.devoops.rentalbrain.business.quote.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quote")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuoteCommandEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long quoteId;

    @Column(name = "counseling_date", nullable = false)
    private LocalDateTime quoteCounselingDate;

    @Column(name = "counselor", nullable = false)
    private String quoteCounselor;

    @Column(name = "summary")
    private String quoteSummary;

    @Column(name = "content")
    @Lob
    private String quoteContent;

    @Column(name = "processing_time", nullable = false)
    private LocalDateTime quoteProcessingTime;

    @Column(name = "channel_id", nullable = false)
    private Integer quoteChannelId;

    @Column(name = "cum_id", nullable = false)
    private Integer quoteCumId;

}
