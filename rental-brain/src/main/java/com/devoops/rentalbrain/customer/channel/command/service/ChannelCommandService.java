package com.devoops.rentalbrain.customer.channel.command.service;

import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandCreateDTO;
import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandResponseDTO;

public interface ChannelCommandService {
    ChannelCommandCreateDTO insertChannel(ChannelCommandCreateDTO channelCommandCreateDTO);

    ChannelCommandResponseDTO updateChannel(Long channelId, ChannelCommandResponseDTO channelCommandResponseDTO);

    ChannelCommandResponseDTO deleteChannel(Long channelId);
}
