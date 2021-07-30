package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.dto.PostBidsRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

	private final RestClientBuilder restClientBuilder;

	public ElasticsearchServiceImpl(RestClientBuilder restClientBuilder) {
		this.restClientBuilder = restClientBuilder;
	}

	@Override
	public ApiStatus logPostBids(PostBidsRequest request) {
		IndexRequest indexRequest = new IndexRequest("bids");
		indexRequest.source(request, XContentType.JSON);
		RestHighLevelClient client = openRestHighLevelClient();

		try {
			IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
			DocWriteResponse.Result responseStatus = response.getResult();

			switch (responseStatus) {
				case CREATED:
				case UPDATED:
					LOGGER.trace("Successfully logged PostBidsRequest to Elasticsearch");
					return ApiStatus.SUCCESS;
				default:
					LOGGER.warn("Problem logging PostBidsRequest to Elasticsearch");
					return ApiStatus.FAILURE;
			}

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return ApiStatus.FAILURE;
		}
	}

	private RestHighLevelClient openRestHighLevelClient() {
		return new RestHighLevelClient(restClientBuilder);
	}
}
