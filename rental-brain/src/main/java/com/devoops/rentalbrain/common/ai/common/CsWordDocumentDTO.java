package com.devoops.rentalbrain.common.ai.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CsWordDocumentDTO {
    private String docId;

    private String chunkId;

    private String keyword;

    private String keywordText;

    private int count;

    private String createdAt;
}
