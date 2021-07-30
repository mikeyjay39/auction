package com.example.auction.controller;

import com.example.auction.contants.ApiStatus;
import com.example.auction.dto.ApiResponse;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.exception.AuctionItemException;
import com.example.auction.service.AuctionItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuctionItemController {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuctionItemController.class);

	private final AuctionItemsService auctionItemsService;

	public AuctionItemController(AuctionItemsService auctionItemsService) {
		this.auctionItemsService = auctionItemsService;
	}

	@PostMapping("/auctionItems")
	public ApiResponse<PostAuctionItemsResponse> postAuctionItems(@RequestBody PostAuctionItemsRequest request) {

		LOGGER.trace("Request received: {}", request.toString());
		ApiResponse<PostAuctionItemsResponse> apiResponse = new ApiResponse<>();

		try {
			PostAuctionItemsResponse response = auctionItemsService.postAuctionItems(request);
			apiResponse.setResult(response);
			apiResponse.setStatus(ApiStatus.SUCCESS.name());
			LOGGER.trace("Request succeeded");
		} catch (AuctionItemException e) {
			LOGGER.error(e.getMessage());
			apiResponse.setStatus(e.getMessage());
		}

		return apiResponse;
	}
}
