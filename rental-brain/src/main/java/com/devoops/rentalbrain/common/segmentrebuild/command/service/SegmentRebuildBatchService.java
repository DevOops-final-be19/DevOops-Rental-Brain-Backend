package com.devoops.rentalbrain.common.segmentrebuild.command.service;


public interface SegmentRebuildBatchService {


    int fixPotentialToNew();

    int fixNewToNormalWithHistory();
    int fixNormalToVipWithHistory();

    int fixToRiskWithHistory();
    // 추가
    int fixRiskToBlacklistWithHistory();


    // 확장 의사 고객(7)
    int fixToExpansionWithHistory();


    // 이탈 위험에서 일반으로
    int fixRiskToNormalWithHistory();

}
