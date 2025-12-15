package com.devoops.rentalbrain.customer.channel.command.service;

import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandCreateDTO;
import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandResponseDTO;
import com.devoops.rentalbrain.customer.channel.command.entity.ChannelCommandEntity;
import com.devoops.rentalbrain.customer.channel.command.repository.ChannelRepository;
import org.springframework.transaction.annotation.Transactional;;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChannelCommandServiceImpl implements ChannelCommandService {

    private final ChannelRepository channelRepository;

    @Autowired
    public ChannelCommandServiceImpl(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    // 채널 생성
    @Override
    @Transactional
    public ChannelCommandCreateDTO insertChannel(ChannelCommandCreateDTO channelCommandCreateDTO) {

        if (channelCommandCreateDTO == null || channelCommandCreateDTO.getChannelName()
                == null || channelCommandCreateDTO.getChannelName().trim().isEmpty()) {
            log.info("채널 생성 실패 dto={}", channelCommandCreateDTO);
            throw new IllegalArgumentException("채널명은 필수입니다.");
        }

        ChannelCommandEntity entity = new ChannelCommandEntity();
        entity.setChannelName(channelCommandCreateDTO.getChannelName().trim());

        ChannelCommandEntity saved = channelRepository.save(entity);

        log.info("채널 생성 완료 channelId={}, channelName={}",
                saved.getChannelId(), saved.getChannelName());

        return new ChannelCommandCreateDTO(
                saved.getChannelId(),
                saved.getChannelName()
        );
    }

    // 채널 수정
    @Override
    @Transactional
    public ChannelCommandResponseDTO updateChannel(Long channelId,
                                                   ChannelCommandResponseDTO channelCommandResponseDTO) {

        if (channelId == null) {
            log.info("채널 수정 실패 channelId가 null");
            throw new IllegalArgumentException("channelId는 필수입니다.");
        }

        ChannelCommandEntity entity = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.info("채널 수정 실패 대상 없음 channelId={}", channelId);
                    return new IllegalArgumentException("해당 채널이 존재하지 않습니다. id=" + channelId);
                });

        String beforeName = entity.getChannelName();
        entity.setChannelName(channelCommandResponseDTO.getChannelName().trim());

        // 변경 감지로 UPDATE 수행
        log.info("채널 수정 완료 channelId={}, before='{}', after='{}'",
                channelId, beforeName, entity.getChannelName());

        return new ChannelCommandResponseDTO(
                entity.getChannelId(),
                entity.getChannelName()
        );
    }

    // 채널 삭제
    @Override
    @Transactional
    public ChannelCommandResponseDTO deleteChannel(Long channelId) {

        if (channelId == null) {
            log.info("채널 삭제 실패 channelId가 null");
            throw new IllegalArgumentException("channelId는 필수입니다.");
        }

        ChannelCommandEntity entity = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.info("채널 삭제 실패 대상 없음 channelId={}", channelId);
                    return new IllegalArgumentException("삭제할 채널이 존재하지 않습니다. id=" + channelId);
                });

        channelRepository.delete(entity);

        log.info("채널 삭제 완료 channelId={}, channelName={}",
                entity.getChannelId(), entity.getChannelName());

        return new ChannelCommandResponseDTO(
                entity.getChannelId(),
                entity.getChannelName()
        );
    }
}
