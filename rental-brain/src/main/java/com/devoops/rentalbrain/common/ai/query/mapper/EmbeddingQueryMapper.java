package com.devoops.rentalbrain.common.ai.query.mapper;

import com.devoops.rentalbrain.common.ai.common.WordDTO;
import com.devoops.rentalbrain.common.ai.query.dto.FeedBackEmbeddingDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmbeddingQueryMapper {

    List<FeedBackEmbeddingDTO> getFeedBacks();


    List<WordDTO> getCsContents();
}
