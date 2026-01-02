package com.devoops.rentalbrain.customer.channel.query.mapper;

import com.devoops.rentalbrain.customer.channel.query.dto.ChannelKpiQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelTotalSumDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ChannelQueryMapper {
    List<ChannelQueryDTO> selectChannelList(@Param("channelName") String channelName);
    List<ChannelKpiQueryDTO> selectChannelKpi(@Param("channelName") String channelName);
    ChannelTotalSumDTO selectChannelTotalSum();

}
