package com.devoops.rentalbrain.common.ai.query.service;

import com.devoops.rentalbrain.common.ai.common.EmbeddingDTO;
import com.devoops.rentalbrain.common.ai.query.dto.FeedBackEmbeddingDTO;
import com.devoops.rentalbrain.common.ai.common.WordDTO;
import com.devoops.rentalbrain.common.ai.query.mapper.EmbeddingQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AiQueryServiceImpl implements AiQueryService {
    private final EmbeddingQueryMapper embeddingQueryMapper;

    public AiQueryServiceImpl(EmbeddingQueryMapper embeddingQueryMapper) {
        this.embeddingQueryMapper = embeddingQueryMapper;
    }

    public List<EmbeddingDTO> getFeedBacks(){
        List<FeedBackEmbeddingDTO> feedBacks = embeddingQueryMapper.getFeedBacks();
        return feedBacks.stream()
                .map(fb->{
                    EmbeddingDTO embeddingDTO = new EmbeddingDTO();
                    embeddingDTO.setChunkId(fb.getFeedBackCode());
                    embeddingDTO.setText(
                            "[고객 피드백]\n제목: " +
                            fb.getTitle() +
                            "\n내용: " +
                            fb.getContent() +
                            "\n조치: " +
                            fb.getAction()
                    );
                    embeddingDTO.setSource("feedback");
                    embeddingDTO.setSourceId(fb.getFeedBackCode());
                    embeddingDTO.setCustomerId(fb.getCumId());
                    embeddingDTO.setSegments(fb.getSegmentName());
                    embeddingDTO.setScore(fb.getStar());
                    embeddingDTO.setCategory(fb.getCategoryName());
                    embeddingDTO.setCreateAt(fb.getCreateDate());
                    return embeddingDTO;
                })
                .collect(Collectors.toList());
    }


    public List<WordDTO> getCs() {
        return embeddingQueryMapper.getCsContents();
    }
}
