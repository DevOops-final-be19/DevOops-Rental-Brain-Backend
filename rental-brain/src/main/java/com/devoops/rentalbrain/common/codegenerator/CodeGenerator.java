package com.devoops.rentalbrain.common.codegenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 비즈니스 코드 생성기
 *
 * 역할:
 * - 테이블별 비즈니스 코드(PREFIX-YYYY-NNN) 생성
 * - id_sequence 테이블을 이용한 연도별 시퀀스 관리
 * - MyBatis 기반 채번 로직을 트랜잭션으로 보호
 *
 * 코드 형식:
 * - PREFIX-YYYY-NNN
 *   예) CUS-2025-001, EMP-2025-010
 *
 * 동시성 전략:
 * - UPDATE + LAST_INSERT_ID() 패턴 사용
 * - 최초 생성 시 INSERT 후 재시도
 */
@Component
@RequiredArgsConstructor
public class CodeGenerator {

    private final IdSequenceMapper codeMapper;

    /**
     * 비즈니스 코드 생성
     *
     * 처리 흐름:
     * 1. 현재 연도 기준으로 시퀀스 증가 시도
     * 2. 대상 row가 없으면 최초 시퀀스 생성 후 재시도
     * 3. 증가된 시퀀스 번호를 이용해 코드 조합
     *
     * @param type 비즈니스 코드 유형(Enum)
     * @return 생성된 비즈니스 코드 (예: CUS-2025-001)
     */
    @Transactional
    public String generate(CodeType type) {
        int year = LocalDate.now().getYear();
        String prefix = type.prefix();

        // 1) 기존 시퀀스 증가 시도
        int updated = codeMapper.incrementLastNumber(prefix, year);

        // 2) 없으면 최초 생성 후 재시도
        if (updated == 0) {
            try {
                codeMapper.insertFirst(prefix, year);
            } catch (Exception ignore) {
                // 동시성 상황: 이미 다른 트랜잭션이 insert 했을 수 있음
            }
            codeMapper.incrementLastNumber(prefix, year);
        }

        // 3) 증가된 시퀀스 값 조회
        int seq = codeMapper.selectLastInsertId();

        return String.format("%s-%d-%03d", prefix, year, seq);
    }
}
