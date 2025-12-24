package com.devoops.rentalbrain.common.ai.command.repository;

import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.KnnQuery;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.SourceConfig;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class OpenSearchVectorRepository {
    private final OpenSearchClient client;
    private final String indexName = "customer_feedback_rag";

    public OpenSearchVectorRepository(OpenSearchClient client) {
        this.client = client;
    }

    public void upsertChunk(String id, Map<String, Object> doc) throws IOException {
        IndexRequest<Map<String, Object>> req = IndexRequest.of(i -> i
                .index(indexName)
                .id(id)
                .document(doc)
        );
        client.index(req);
    }

    public SearchResponse<Map> knnSearch(List<Float> queryVector, int k) throws IOException {
        float[] queryVectorList = new float[queryVector.size()];
        for (int i = 0; i < queryVector.size(); i++) {
            queryVectorList[i] = queryVector.get(i);
        }

        KnnQuery knn = KnnQuery.of(q -> q
                .field("vector")
                .vector(queryVectorList)
                .k(k)
        );

        SearchRequest req = SearchRequest.of(s -> s
                .index(indexName)
                .source(SourceConfig.of(sc -> sc.filter(f -> f.includes("text", "docId", "chunkId", "meta"))))
                .query(q -> q.knn(knn))
        );


        return client.search(req, Map.class);
    }
}
