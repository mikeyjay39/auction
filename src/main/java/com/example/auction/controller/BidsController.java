package com.example.auction.controller;

import com.example.auction.dto.ApiResponse;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.exception.PostBidsException;
import com.example.auction.service.BidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bids")
public class BidsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BidsController.class);

	private final BidService bidService;

	public BidsController(BidService bidService) {
		this.bidService = bidService;
	}

	@PostMapping
	public ApiResponse<AuctionItemDto> postBids(@RequestBody PostBidsRequest request) {
		return doPostBids(request);
	}

	private ApiResponse<AuctionItemDto> doPostBids(PostBidsRequest request) {
		LOGGER.trace("Request received: {}", request);

		try {
			ApiResponse<AuctionItemDto> response = bidService.postBids(request);
			LOGGER.trace("Request succeeded");
			return response;
		} catch (PostBidsException e) {
			LOGGER.error(e.getMessage());
			ApiResponse<AuctionItemDto> response = new ApiResponse<>();
			response.setStatus(e.getMessage());
			return response;
		}
	}
}
