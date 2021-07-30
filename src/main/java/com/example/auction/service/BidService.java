package com.example.auction.service;

import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.exception.PostBidsException;

public interface BidService {

	AuctionItemDto postBids(PostBidsRequest request) throws PostBidsException;
}
