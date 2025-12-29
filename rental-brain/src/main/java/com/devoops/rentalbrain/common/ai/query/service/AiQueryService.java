package com.devoops.rentalbrain.common.ai.query.service;

import com.devoops.rentalbrain.common.ai.common.EmbeddingDTO;
import com.devoops.rentalbrain.common.ai.common.WordDTO;

import java.util.List;

public interface AiQueryService {
    List<EmbeddingDTO> getFeedBacks();

    List<WordDTO> getCs();
}
