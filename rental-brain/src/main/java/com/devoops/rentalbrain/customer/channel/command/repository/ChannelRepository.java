package com.devoops.rentalbrain.customer.channel.command.repository;

import com.devoops.rentalbrain.customer.channel.command.entity.ChannelCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<ChannelCommandEntity, Long> {
}
