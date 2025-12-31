package com.devoops.rentalbrain.common.ai.command.repository;

import com.devoops.rentalbrain.common.ai.command.dto.KeywordCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Repository;
import org.opensearch.client.opensearch._types.aggregations.Aggregate;


import java.io.IOException;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class OpenSearchVectorRepository {
    private final OpenSearchClient client;
    private final String customerInteractionsIndex = "rag_customer_interactions_v1";
    private final String customerInteractionsPipeLine = "rag_metadata_pipeline";
    private final String csKeywordIndex = "keyword_stats_v1";

    public OpenSearchVectorRepository(OpenSearchClient client) {
        this.client = client;
    }

    public void upsertChunk(String id, Map<String, Object> doc) throws IOException {
        IndexRequest req = IndexRequest.of(i -> i
                .index(customerInteractionsIndex)
                .id(id)
                .pipeline(customerInteractionsPipeLine)
                .document(doc)
        );
        client.index(req);
        log.info("upsert done: {} ", id);
    }

    public void upsertWords(Map<String, Object> doc) throws IOException {
        IndexRequest req = IndexRequest.of(i -> i
                .index(csKeywordIndex)
                .document(doc)
        );
        client.index(req);
        log.info("upsert done: {} ", req);
    }

//    public SearchResponse<Map> knnSearch(List<Float> queryVector, int k) throws IOException {
//        float[] queryVectorList = new float[queryVector.size()];
//        for (int i = 0; i < queryVector.size(); i++) {
//            queryVectorList[i] = queryVector.get(i);
//        }
//
//        KnnQuery knn = KnnQuery.of(q -> q
//                .field("embedding")
//                .vector(queryVectorList)
//                .k(k)
//        );
//
//        SearchRequest req = SearchRequest.of(s -> s
//                .index(indexName)
//                .source(SourceConfig.of(sc -> sc.filter(f -> f.includes("text", "chunkId", "metadata"))))
//                .query(q -> q.knn(knn))
//        );
//
//
//        return client.search(req, Map.class);
//    }

    public SearchResponse<Map> knnSearchWithFilter(
            float[] vector,
            int k,
            Map<String, Object> filter
    ) throws IOException {

        List<Query> filters = buildFilters(filter);

        return client.search(s -> s
                        .index(customerInteractionsIndex)
                        .size(k)
                        .query(q -> q
                                .bool(b -> b
                                        .filter(filters)
                                        .must(m -> m.knn(knn -> knn
                                                .field("embedding")
                                                .vector(vector)
                                                .k(k)
                                        ))
                                ))
                        .source(src -> src
                                .filter(f -> f
                                        .includes("text", "chunkId", "category", "sentiment", "segments", "vocab", "responseStyle")
                                )
                        ),
                Map.class
        );
    }

    private List<Query> buildFilters(Map<String, Object> filter) {

        List<Query> filters = new ArrayList<>();

        // 날짜 범위
        if (filter.containsKey("from") && filter.containsKey("to")) {
            filters.add(Query.of(q -> q
                    .range(r -> r
                            .field("metadata.createdAt")
                            .gte(JsonData.of(filter.get("from")))
                            .lte(JsonData.of(filter.get("to")))
                    )
            ));
        }

        // sentiment 필터
        if (filter.containsKey("sentiment")) {
            filters.add(Query.of(q -> q
                    .term(t -> t
                            .field("metadata.sentiment")
                            .value(FieldValue.of(filter.get("sentiment").toString()))
                    )
            ));
        }

        // category 필터 (있다면)
        if (filter.containsKey("category")) {
            filters.add(Query.of(q -> q
                    .term(t -> t
                            .field("metadata.category")
                            .value(FieldValue.of(filter.get("category").toString()))
                    )
            ));
        }

        return filters;
    }

    private Query term(String field, Object value) {
        return Query.of(q -> q.term(t -> t
                .field(field)
                .value(FieldValue.of(value.toString()))
        ));
    }

    public List<KeywordCountDTO> getTopKeywords(String sentiment, int size, String yearMonth) throws IOException {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SearchResponse<Void> response =
                client.search(s -> s
                                .index(customerInteractionsIndex)
                                .size(0)
                                .query(q -> q
                                        .bool(b -> b
                                                .filter(f -> f
                                                        .range(r -> r
                                                                .field("metadata.createdAt")
                                                                .gte(JsonData.of(
                                                                                YearMonth.parse(yearMonth)
                                                                                        .atDay(1)
                                                                                        .atStartOfDay()
                                                                                        .format(formatter)
                                                                        )
                                                                )
                                                                .lte(JsonData.of(
                                                                                YearMonth.parse(yearMonth)
                                                                                        .atEndOfMonth()
                                                                                        .atTime(23, 59, 59)
                                                                                        .format(formatter)
                                                                        )
                                                                )
                                                        )
                                                )
                                                .filter(f -> f
                                                        .term(t -> t
                                                                .field("metadata.sentiment")
                                                                .value(FieldValue.of(sentiment))
                                                        )
                                                )
                                        )
                                )
                                .aggregations("top_vocab", a -> a
                                        .terms(t -> t
                                                .field("vocab")
                                                .size(size)
                                        )
                                ),
                        Void.class
                );

        Aggregate agg =
                response.aggregations()
                        .get("top_vocab");

        return agg.sterms().buckets().array().stream()
                .map(b -> new KeywordCountDTO(
                        b.key(),
                        b.docCount()
                ))
                .toList();
    }

    public List<KeywordCountDTO> getTopCsKeywords(int size, String yearMonth) throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                        .index(csKeywordIndex)
                        .size(0)
                        .query(q -> q
                                .range(r -> r
                                        .field("created_at")
                                        .gte(JsonData.of(
                                                        YearMonth.parse(yearMonth)
                                                                .atDay(1)
                                                                .atStartOfDay()
                                                                .atOffset(ZoneOffset.of("+09:00"))
                                                                .toString()
                                                )
                                        )
                                        .lte(JsonData.of(
                                                        YearMonth.parse(yearMonth)
                                                                .atEndOfMonth()
                                                                .atTime(23, 59, 59)
                                                                .atOffset(ZoneOffset.of("+09:00"))
                                                                .toString()
                                                )
                                        )

                                )
                        )
                        .aggregations("top_keywords", a -> a
                                .terms(t -> t
                                        .field("keyword")
                                        .size(size)
                                )
                        ),
                Void.class
        );

        Aggregate agg =
                response.aggregations()
                        .get("top_keywords");

        return agg.sterms().buckets().array().stream()
                .map(b -> new KeywordCountDTO(
                        b.key(),
                        b.docCount()
                ))
                .toList();
    }
}
