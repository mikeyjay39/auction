package com.example.auction.service;

import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.exception.AuctionItemException;

import java.util.List;

public interface AuctionItemsService {

	PostAuctionItemsResponse postAuctionItems(PostAuctionItemsRequest request) throws AuctionItemException;

	List<AuctionItemDto> getAuctionItems();

	AuctionItemDto getAuctionItem(String id);
}
