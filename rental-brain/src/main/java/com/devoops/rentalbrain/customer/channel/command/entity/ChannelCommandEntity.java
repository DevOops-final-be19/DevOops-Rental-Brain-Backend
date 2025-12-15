package com.devoops.rentalbrain.customer.channel.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "channel")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChannelCommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long channelId;

    @Column(name = "name")
    private String channelName;
}
