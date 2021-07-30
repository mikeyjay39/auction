package com.example.auction.service;

import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.exception.AuctionItemException;

public interface AuctionItemsService {

	PostAuctionItemsResponse postAuctionItems(PostAuctionItemsRequest request) throws AuctionItemException;
}
