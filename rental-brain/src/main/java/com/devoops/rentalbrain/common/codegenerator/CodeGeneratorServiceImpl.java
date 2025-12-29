package com.devoops.rentalbrain.common.codegenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    private final IdSequenceMapper idSequenceMapper;

    @Override
    @Transactional
    public String nextCode(String domain, int width) {

        int year = Year.now().getValue();

//        -- 1) 시퀀스 증가 또는 최초 생성
        idSequenceMapper.nextSequence(domain, year);

//        -- 2) 증가된 값 조회 (같은 커넥션)
        int seq = idSequenceMapper.selectLastInsertId();

//        -- 3) 포맷팅
        String padded = String.format("%0" + width + "d", seq);
        return domain + "-" + year + "-" + padded;
    }
}
