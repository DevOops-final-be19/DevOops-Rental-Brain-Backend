package com.devoops.rentalbrain.customer.channel.query.service;

import com.devoops.rentalbrain.customer.channel.query.dto.ChannelKpiQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelTotalSumDTO;

import java.util.List;

public interface ChannelQueryService {
    List<ChannelQueryDTO> selectChannelList(String channelName);
    List<ChannelKpiQueryDTO> selectChannelKpi(String channelName);
    ChannelTotalSumDTO selectChannelTotalSum();

}
