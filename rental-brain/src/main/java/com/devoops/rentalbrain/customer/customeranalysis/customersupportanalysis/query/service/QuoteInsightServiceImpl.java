package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.QuoteInsightRowDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.mapper.QuoteInsightQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseOutputText;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@Slf4j
public class QuoteInsightServiceImpl implements QuoteInsightService {

    private final QuoteInsightQueryMapper quoteInsightQueryMapper;

    public QuoteInsightServiceImpl(QuoteInsightQueryMapper quoteInsightQueryMapper) {
        this.quoteInsightQueryMapper = quoteInsightQueryMapper;
    }

    @Override
    public String analyzeQuoteInsight(LocalDate start, LocalDate end, int windowDays, int sampleEach) {
        if (start == null || end == null) throw new IllegalArgumentException("start/end는 필수입니다.");
        if (end.isBefore(start)) throw new IllegalArgumentException("end는 start보다 빠를 수 없습니다.");
        if (windowDays < 1 || windowDays > 365) throw new IllegalArgumentException("windowDays는 1~365 범위로 설정하세요.");
        if (sampleEach < 5) sampleEach = 5;
        if (sampleEach > 200) sampleEach = 200;

        LocalDateTime startDt = start.atStartOfDay();
        LocalDateTime endExclusive = end.plusDays(1).atStartOfDay(); // end 포함 처리

        List<QuoteInsightRowDTO> rows = quoteInsightQueryMapper.selectQuoteInsightRows(startDt, endExclusive, windowDays);
        if (rows == null || rows.isEmpty()) throw new IllegalArgumentException("해당 기간에 견적/상담 데이터가 없습니다.");

        List<QuoteInsightRowDTO> success = rows.stream().filter(r -> r.getIsSuccess() != null && r.getIsSuccess() == 1).collect(Collectors.toList());
        List<QuoteInsightRowDTO> fail = rows.stream().filter(r -> r.getIsSuccess() == null || r.getIsSuccess() == 0).collect(Collectors.toList());

        // 샘플링
        List<QuoteInsightRowDTO> sampled = new ArrayList<>();
        sampled.addAll(sample(success, sampleEach, 42));
        sampled.addAll(sample(fail, sampleEach, 84));

        String inputJsonLines = sampled.stream().map(this::toCompactJsonLine).collect(Collectors.joining("\n"));
        String prompt = buildPrompt(start, end, windowDays, success.size(), fail.size(), sampled.size(), inputJsonLines);
        log.info("[QuoteInsight] prompt length={}", prompt.length());

        OpenAIClient client = OpenAIOkHttpClient.fromEnv();
        ResponseCreateParams createParams = ResponseCreateParams.builder()
                .temperature(0.0)
                .topP(1.0)
                .input(prompt)
                .model(ChatModel.GPT_5_1)
                .build();

        Response response = client.responses().create(createParams);
        String result = extractText(response);

        if (result == null || result.isBlank()) throw new IllegalStateException("AI 응답이 비어있습니다.");
        return result.trim();
    }

    private List<QuoteInsightRowDTO> sample(List<QuoteInsightRowDTO> source, int n, int seed) {
        if (source == null || source.isEmpty()) return Collections.emptyList();
        List<QuoteInsightRowDTO> copy = new ArrayList<>(source);
        Collections.shuffle(copy, new Random(seed));
        return copy.size() <= n ? copy : copy.subList(0, n);
    }

    private String toCompactJsonLine(QuoteInsightRowDTO r) {
        String summary = safeTrim(r.getQuoteSummary(), 160);
        String content = safeTrim(r.getQuoteContent(), 400);
        String customer = safeTrim(r.getCustomerName(), 40);
        String channel = safeTrim(r.getChannelName(), 20);

        return "{" +
                "\"isSuccess\":" + (r.getIsSuccess() != null ? r.getIsSuccess() : 0) + "," +
                "\"customer\":" + jsonString(customer) + "," +
                "\"channel\":" + jsonString(channel) + "," +
                "\"summary\":" + jsonString(summary) + "," +
                "\"content\":" + jsonString(content) +
                "}";
    }


    private String buildPrompt(LocalDate start, LocalDate end, int windowDays,
                               int successTotal, int failTotal, int sampledTotal, String inputJsonLines) {
        return "SYSTEM:\n" +
                "You are a strict JSON generation engine for business insight dashboards.\n" +
                "You must output ONLY valid JSON (no markdown, no explanations).\n\n" +
                "USER:\n" +
                "다음은 견적/상담 텍스트 데이터(요약/내용)이며, 각 행에는 isSuccess(1=성공,0=실패) 라벨이 있습니다.\n" +
                "성공 판정은 '견적일 이후 " + windowDays + "일 이내 계약 시작일 존재' 입니다.\n" +
                "분석 기간: " + start + " ~ " + end + "\n" +
                "전체 라벨 분포(참고): success=" + successTotal + ", fail=" + failTotal + "\n" +
                "이번 요청에는 샘플 " + sampledTotal + "건만 제공됩니다.\n\n" +
                "반드시 아래 JSON 스키마로만 응답하세요. (모든 텍스트는 한국어)\n" +
                "{\n" +
                "  \"generatedAt\": string,\n" +
                "  \"successFactorsTop3\": [{\"rank\": number, \"label\": string, \"count\": number}],\n" +
                "  \"failFactorsTop3\":    [{\"rank\": number, \"label\": string, \"count\": number}],\n" +
                "  \"positiveKeywords\":    [{\"rank\": number, \"label\": string, \"count\": number}],\n" +
                "  \"complaintTop3\":       [{\"rank\": number, \"label\": string, \"count\": number}],\n" +
                "  \"notes\":               string[]\n" +
                "}\n\n" +
                "규칙:\n" +
                "- successFactorsTop3: 성공(1) 데이터에서 반복적으로 나타나는 성공 요인 TOP3\n" +
                "- failFactorsTop3: 실패(0) 데이터에서 반복적으로 나타나는 실패 요인 TOP3\n" +
                "- positiveKeywords: 긍정 피드백/장점 키워드 TOP5 (rank 1~5)\n" +
                "- complaintTop3: 불만/컴플레인 원인 TOP3\n" +
                "- count는 샘플 내 빈도를 기반으로 산정하세요(정수).\n" +
                "- label은 2~12자 내의 짧은 문구로 정리(예: '맞춤 제안', '가격 경쟁력', 'AS 지연').\n" +
                "- notes는 2~4개로, 실무 액션 힌트를 포함한 한 줄 문장으로 작성.\n" +
                "- JSON 외 어떤 문자도 출력하지 마세요.\n\n" +
                "INPUT (JSON Lines):\n" + inputJsonLines + "\n";
    }

    private String extractText(Response response) {
        try {
            return response.output().stream()
                    .flatMap(item -> item.message().stream())
                    .flatMap(message -> message.content().stream())
                    .flatMap(content -> content.outputText().stream())
                    .map(ResponseOutputText::text)
                    .filter(s -> s != null && !s.equals("```html") && !s.equals("```") && !s.equals("```json"))
                    .collect(Collectors.joining(""));
        } catch (Exception e) {
            log.error("[QuoteInsight] extractText failed", e);
            return null;
        }
    }

    private String safeTrim(String s, int max) {
        if (s == null) return "";
        String t = s.trim();
        return t.length() <= max ? t : t.substring(0, max);
    }

    private String jsonString(String s) {
        if (s == null) return "\"\"";
        String escaped = s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("\t", " ");
        return "\"" + escaped + "\"";
    }


}
