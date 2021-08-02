package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.test.ElasticsearchTestFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

class ElasticsearchServiceImplTestIT extends ElasticsearchTestFactory {

	@Autowired
	private ElasticsearchServiceImpl elasticsearchService;

	@Test
	void logPostBids() throws Exception {
		logPostBidsTest();
	}

	private void logPostBidsTest() throws Exception {
		PostBidsRequest postBidsRequest = new PostBidsRequest();
		postBidsRequest.setBidderName("example");
		postBidsRequest.setMaxAutoBidAmount(BigDecimal.ONE);
		postBidsRequest.setAuctionItemId("1");
		CompletableFuture<ApiStatus> statusFuture = elasticsearchService.logPostBids(postBidsRequest);
		ApiStatus status = statusFuture.get();
		Assert.assertEquals(ApiStatus.SUCCESS, status);
	}
}