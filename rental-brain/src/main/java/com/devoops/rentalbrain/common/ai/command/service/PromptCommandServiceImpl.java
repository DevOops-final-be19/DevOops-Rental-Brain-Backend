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

    public String keywordExtractPrompt(String content){
        return """
                역할: 너는 한국어 고객문의 문장에서 "검색/집계용 키워드"를 추출하는 엔진이다.
                
                입력 문장:
                %s
                
                규칙:
                - 반드시 JSON만 출력한다. (설명/코드블록 금지)
                - 키워드는 2~8개
                - 각 키워드는 1~4단어의 짧은 구/명사형
                - 불용어(가능/문의/부탁/궁금/관련/해요/있나요 등) 제거
                - 의미 중복/동의어는 하나로 합치고 가장 일반적인 표현으로 통일
                - 브랜드/제품/기술 용어(예: VPN, GPU, NAS, L3 스위치)는 그대로 유지
                - 출력에 영어/숫자 포함 가능
                - "keywords"는 배열, "keyword_text"는 공백으로 join한 문자열
                
                출력 JSON 스키마:
                {
                  "keywords": ["...", "..."],
                  "keyword_text": "..."
                }
                
                """.formatted(content);
    }
}
