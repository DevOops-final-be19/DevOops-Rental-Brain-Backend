package com.devoops.rentalbrain.config;

import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.RestClient;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {
    @Bean
    public OpenSearchClient openSearchClient(
            @Value("${opensearch.host}") String host,
            @Value("${opensearch.port}") int port
    ) {

        // OpenSearch 전용 RestClient
        RestClient restClient = RestClient.builder(
                new org.apache.http.HttpHost(host, port, "http")
        ).build();

        // Jackson mapper
        RestClientTransport transport =
                new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new OpenSearchClient(transport);
    }
}
