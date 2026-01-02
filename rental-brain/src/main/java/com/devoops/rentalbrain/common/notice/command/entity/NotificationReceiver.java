package com.devoops.rentalbrain.common.notice.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="notification_receiver")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationReceiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Character isRead;
    @Column
    private String createdAt;
    @Column
    private String readAt;
    @Column
    private Long notiId;
    @Column
    private Long empId;

    public static NotificationReceiver create(Long notiId, Long empId) {
        NotificationReceiver r = new NotificationReceiver();
        r.notiId = notiId;
        r.empId = empId;
        r.isRead = 'N';
        r.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        r.readAt = null;
        return r;
    }
}
