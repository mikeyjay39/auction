package com.example.auction.service;

import com.example.auction.dto.ApiResponse;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.exception.PostBidsException;

public interface BidService {

	ApiResponse<AuctionItemDto> postBids(PostBidsRequest request) throws PostBidsException;
}
