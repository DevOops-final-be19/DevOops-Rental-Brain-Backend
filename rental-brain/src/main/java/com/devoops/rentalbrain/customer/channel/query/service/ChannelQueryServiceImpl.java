package com.devoops.rentalbrain.customer.channel.query.service;

import com.devoops.rentalbrain.customer.channel.query.dto.ChannelKpiQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelQueryDTO;
import com.devoops.rentalbrain.customer.channel.query.dto.ChannelTotalSumDTO;
import com.devoops.rentalbrain.customer.channel.query.mapper.ChannelQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChannelQueryServiceImpl implements ChannelQueryService {

    private final ChannelQueryMapper channelQueryMapper;

    @Autowired
    public ChannelQueryServiceImpl(ChannelQueryMapper channelQueryMapper) {
        this.channelQueryMapper = channelQueryMapper;
    }

    @Override
    public List<ChannelQueryDTO> selectChannelList(String channelName) {
        List<ChannelQueryDTO> list = channelQueryMapper.selectChannelList(channelName);

        log.info("채널 목록 조회 - 현재 채널의 갯수: {}건", list.size());
        return list;
    }

    @Override
    public List<ChannelKpiQueryDTO> selectChannelKpi(String channelName) {
        List<ChannelKpiQueryDTO> list = channelQueryMapper.selectChannelKpi(channelName);

        log.info("채널별 카운트 조회 - 기능별 각 채널 수: {}개", list.size());
        return list;
    }

    @Override
    public ChannelTotalSumDTO selectChannelTotalSum() {
        ChannelTotalSumDTO sum = channelQueryMapper.selectChannelTotalSum();

        log.info("전체 채널, 고객: {}, 상담: {}, 피드백: {}, 문의: {}, 토탈: {}",
                sum.getCustomerCount(),
                sum.getQuoteCount(),
                sum.getFeedbackCount(),
                sum.getCustomerSupportCount(),
                sum.getTotalCount()
        );

        return sum;
    }
}
