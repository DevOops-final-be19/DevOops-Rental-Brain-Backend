package com.devoops.rentalbrain.common.notice.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column
    private String type;
    @Column
    private String title;
    @Column
    private String message;
    @Column
    private String linkUrl;

    public Notification(String type, String title, String message, String linkUrl) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.linkUrl = linkUrl;
    }
}
