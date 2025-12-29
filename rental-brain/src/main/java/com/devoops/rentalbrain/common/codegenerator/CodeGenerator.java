package com.devoops.rentalbrain.common.codegenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 비즈니스 코드 생성기
 * 역할:
 * - 테이블별 비즈니스 코드(PREFIX-YYYY-NNN) 생성
 * - id_sequence 테이블을 이용한 연도별 시퀀스 관리
 * - MyBatis 기반 채번 로직을 트랜잭션으로 보호
 * 코드 형식:
 * - PREFIX-YYYY-NNN
 *   예) CUS-2025-001, EMP-2025-010
 * 동시성 전략:
 * - INSERT ... ON DUPLICATE KEY UPDATE + LAST_INSERT_ID() 패턴 사용
 * - row가 없으면 INSERT로 생성, 있으면 UPDATE로 증가 (한 번에 처리)
 */
@Component
@RequiredArgsConstructor
public class CodeGenerator {

    private final IdSequenceMapper codeMapper;

    /**
     * 비즈니스 코드 생성
     * 처리 흐름:
     * 1) (prefix, year) 시퀀스를 "생성 또는 증가" (한 방 쿼리)
     * 2) 같은 커넥션에서 LAST_INSERT_ID()로 증가된 번호 조회
     * 3) PREFIX-YYYY-NNN 포맷으로 코드 생성
     *
     * @param type 비즈니스 코드 유형(Enum)
     * @return 생성된 비즈니스 코드 (예: CUS-2025-001)
     */
    @Transactional
    public String generate(CodeType type) {
        int year = LocalDate.now().getYear();
        String prefix = type.prefix();

        // 1) 생성 또는 증가 (동시성 안전)
        codeMapper.nextSequence(prefix, year);

        // 2) 증가된 값 조회 (반드시 같은 트랜잭션/커넥션)
        int seq = codeMapper.selectLastInsertId();

        // 3) 코드 조합 (기본 3자리)
        return String.format("%s-%d-%03d", prefix, year, seq);
    }
}
