package com.example.auction.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
public class ElasticsearchConfig {

	@Value("${elastic.search.server}")
	private String elasticSearchUrl;

	@Value("${elastic.search.server.port}")
	private Integer elasticSearchPort;

	@Bean
	public RestHighLevelClient client() {
		final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(String.format("%s:%s", elasticSearchUrl, elasticSearchPort))
				.build();

		return RestClients.create(clientConfiguration).rest();
	}
}
