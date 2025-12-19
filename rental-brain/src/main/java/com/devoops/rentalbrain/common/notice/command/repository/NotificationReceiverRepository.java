package com.devoops.rentalbrain.common.notice.command.repository;

import com.devoops.rentalbrain.common.notice.command.entity.NotificationReceiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationReceiverRepository extends JpaRepository<NotificationReceiver,Long> {
}
