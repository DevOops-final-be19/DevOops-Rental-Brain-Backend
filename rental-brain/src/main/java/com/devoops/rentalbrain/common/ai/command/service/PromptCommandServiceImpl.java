package com.devoops.rentalbrain.common.ai.command.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PromptCommandServiceImpl implements PromptCommandService {


    public String buildPrompt(List<String> ctx, String q, String style) {
        String rule = switch (style) {
            case "summary" -> "핵심 이슈 위주로 요약하라.";
            case "list" -> "항목별로 정리하라.";
            default -> "이유를 설명하라.";
        };

        return """
                SYSTEM:
                너는 회사 내부 고객 피드백 데이터만을 근거로 답변하는 RAG 시스템이다.
                추측, 일반론, 조언, 학습 가이드는 절대 제공하지 마라.
                
                RULE:
                %s
                
                CONTEXT:
                %s
                
                QUESTION:
                %s
                """.formatted(rule, String.join("\n---\n", ctx), q);
    }

    public String buildQueryMetadataPrompt(String question) {
        return """
                너는 회사 내부 "고객 피드백 데이터"를 검색하는 RAG 질문 분석기다.
                질문은 모두 고객 피드백을 대상으로 한다.
                
                반드시 아래 JSON 형식으로만 응답하라.
                JSON 외 텍스트는 절대 출력하지 마라.
                
                규칙:
                - JSON 외 텍스트 금지
                - responseStyle:
                  - 요약, 정리 → summary
                  - 목록, 나열 → list
                  - 이유, 왜 → explain
                
                출력 형식:
                {
                  "category": null | "서비스 만족" | "제품 불량" | "제품 품질" | "AS 지연" | "직원 응대" | "서비스 불만"
                  "sentiment": null | "긍정" | "부정" | "중립",
                  "vocab": [],
                  "segments": null | 잠재 고객 | 신규 고객 | 일반 고객 | 이탈 위험 고객 | VIP 고객 | 블랙리스트 고객 | 확장 의사 고객 (기회 고객),
                  "responseStyle": "summary | list | explain"
                }
                
                질문:
                \"\"\"%s\"\"\"
                """.formatted(question);
    }

    public String buildVocabSentimentPrompt(String text) {
        return """
                당신은 고객 피드백 분석 엔진입니다.
                
                아래 텍스트에서 "감정"과 "이슈를 나타내는 핵심 구(phrase)"만 추출해서 JSON 형식으로 반환하세요.
                
                규칙:
                - 2단어 이상으로 의미가 완성되는 표현은 유지
                  (예: 제품 불량, 서비스 질 저하)
                - 감정 강도 표현은 키워드에서 제외
                  (예: 매우 만족, 매우 빠름, 아주 좋음, 매우 불만, 매우 느림, 아주 나쁨)
                - 강조어(매우, 아주, 상당히)는 제거
                - 이슈의 대상 + 상태가 명확한 표현만 유지
                  (예: 응대 속도, 서비스 품질 저하, 제품 불량)
                - 단독으로 의미가 약한 일반 단어는 제외
                  (예: 제품, 서비스, 만족)
                - 불용어가 포함되더라도 전체가 이슈라면 유지
                - 명사/형용사 중심
                - 최대 6개
                - JSON 외 출력 금지
                - 중복 단어 제거
                - 감정은 문맥 기준으로 판단
                
                출력 형식:
                {
                  "vocab": [],
                  "sentiment": "긍정 | 중립 | 부정"
                }
                
                텍스트:
                \"\"\"
                %s
                \"\"\"
                """.formatted(text);
    }

    public String keywordExtractPrompt(String content) {
        return """
                너는 고객 상담(CS) 키워드를 정규화하는 AI다.
                
                   아래에 정의된 “대표 키워드 사전”을 반드시 기준으로 삼아야 한다.
                   입력 문장에서 키워드를 추출할 때,
                   의미가 같은 경우에는 반드시 대표 키워드로 치환하여 출력하라.
                
                   ❗ 절대 규칙:
                   1. 출력 키워드는 반드시 아래 “대표 키워드” 중 하나이거나,
                      사전에 없는 경우에만 새로운 키워드를 생성할 수 있다
                   2. 사전에 있는 키워드와 의미적으로 겹치면
                      절대 새로운 키워드를 만들지 마라
                   3. 같은 의미의 키워드는 하나만 출력한다
                   4. 키워드는 1~3단어 명사 또는 명사구로 한다
                   5. 반드시 최소 1개 이상 키워드를 출력해야 한다
                   6. 출력은 JSON 형식만 허용한다
                   7. 설명 문장은 절대 출력하지 않는다
                
                   ---
                
                   ### 📘 대표 키워드 사전 (Canonical Dictionary)
                
                   [계약 관리]
                   - 계약 기간
                   - 계약 기간 단축
                   - 계약 시작일
                   - 계약 시점
                   - 계약 연장
                   - 계약 조건
                   - 계약 조건 변경 절차
                   - 계약서 조항
                   - 계약서 사본 재발급
                   - 장비 단위 계약
                   - 연간 계약
                
                   [계약 해지]
                   - 부분 해지
                   - 해지 절차
                   - 서비스 해지 절차
                   - 자동 갱신 해지 조건
                
                   [위약금]
                   - 위약금 조건
                   - 중도 해지 위약금
                   - 위약금 없이 해지 조건
                
                   [가격 정책]
                   - 가격 인상
                   - 가격 차이
                   - 요금 정책
                   - 요금 산정 방식
                   - 단가
                   - 단가 할인
                   - 최소 금액 조건
                
                   [할인 정책]
                   - 장기 계약 할인
                   - 장기 고객 유지 할인 정책
                   - 장기 프로젝트 할인
                   - 재계약 할인 조건
                   - 공공기관 할인
                   - 교육 기관 할인 적용 조건
                   - 패키지 할인
                   - 추가 할인
                   - 할인율
                   - 연말 프로모션 할인
                   - 프로모션 중복 적용
                
                   [VPN]
                   - VPN 접속 장애
                   - VPN 인증 서버 응답 지연
                   - VPN 보안 게이트웨이
                
                   [네트워크 장애]
                   - 네트워크 속도 저하
                   - 네트워크 지연 시간
                   - 네트워크 설정
                   - 스위치 포트 장애
                
                   [서버 장애]
                   - 서버 접속 장애
                   - 서버 메모리 오류
                   - 서버 팬 소음
                   - 서버 랙 과열 경고
                   - 서버 랙 온도 관리 시스템
                   - 부팅 실패
                   - 서버 OS 패치
                
                   [스토리지 장애]
                   - 스토리지 RAID 상태 경고
                   - 스토리지 볼륨 인식 오류
                   - 디스크 I/O 지연
                
                   [백업]
                   - 백업 복구 실패
                   - 백업 스케줄 미동작
                   - 백업 서비스
                   - 백업 데이터 암호화
                
                   [업데이트 실패]
                   - 펌웨어 업데이트 실패
                   - 펌웨어 자동 업데이트 실패
                   - POS 장비 업데이트 실패
                   - 적용 오류
                
                   [로그/모니터링]
                   - 로그 보관 기간
                   - 시스템 로그 누락
                   - 로그 수집 에이전트 비정상 종료
                   - 고급 모니터링 알림 설정
                   - 모니터링 대시보드 커스터마이징
                
                   [AI 분석]
                   - AI 로그 분석 기능
                   - 장애 예측 기능
                
                   [장비 옵션]
                   - GPU 옵션
                   - 장비 교체 옵션
                   - 추가 장비 도입
                
                   [장비 설정 오류]
                   - 장비 설정 오류
                   - 장비 동기화 오류
                   - 장비 위치 변경
                
                ### 출력 형식
                
                {
                  "keywords": ["키워드1", "키워드2"]
                }
                
                ---
                
                분석 대상 텍스트:
                \"\"\"
                %s
                \"\"\"
                
                """.formatted(content);
    }
}
