package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.test.ElasticsearchTestFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

class ElasticsearchServiceImplTestIT extends ElasticsearchTestFactory {

	@Autowired
	private ElasticsearchServiceImpl elasticsearchService;

	@Test
	void logPostBids() {
		logPostBidsTest();
	}

	private void logPostBidsTest() {
		PostBidsRequest postBidsRequest = new PostBidsRequest();
		postBidsRequest.setBidderName("example");
		postBidsRequest.setMaxAutoBidAmount(BigDecimal.ONE);
		postBidsRequest.setAuctionItemId("1");
		ApiStatus status = elasticsearchService.logPostBids(postBidsRequest);
		Assert.assertEquals(ApiStatus.SUCCESS, status);
	}
}