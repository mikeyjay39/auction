package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.dto.ElasticsearchBidDto;
import com.example.auction.dto.PostBidsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;


@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

	private final RestHighLevelClient client;
	private final ObjectMapper mapper;

	public ElasticsearchServiceImpl(RestHighLevelClient client, ObjectMapper mapper) {
		this.client = client;
		this.mapper = mapper;
	}

	@Override
	@Async
	public CompletableFuture<ApiStatus> logPostBids(PostBidsRequest request) {
		IndexRequest indexRequest = new IndexRequest("bids");
		ElasticsearchBidDto dto = convertToBidDto(request);

		try {
			indexRequest.source(mapper.writeValueAsString(dto), XContentType.JSON);
			IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
			DocWriteResponse.Result responseStatus = response.getResult();

			switch (responseStatus) {
				case CREATED:
				case UPDATED:
					LOGGER.trace("Successfully logged PostBidsRequest to Elasticsearch");
					return CompletableFuture.completedFuture(ApiStatus.SUCCESS);
				default:
					LOGGER.warn("Problem logging PostBidsRequest to Elasticsearch");
					return CompletableFuture.completedFuture(ApiStatus.FAILURE);
			}

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return CompletableFuture.completedFuture(ApiStatus.FAILURE);
		}
	}

	private ElasticsearchBidDto convertToBidDto(PostBidsRequest request) {
		ElasticsearchBidDto dto = new ElasticsearchBidDto();
		dto.setAuctionItemId(request.getAuctionItemId());
		dto.setBidderName(request.getBidderName());
		dto.setMaxAutoBidAmount(request.getMaxAutoBidAmount());
		dto.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		return dto;
	}
}
