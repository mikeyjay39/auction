package com.example.auction.service;

import com.example.auction.domain.AuctionItem;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.exception.AuctionItemException;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface AuctionItemsService {

	PostAuctionItemsResponse postAuctionItems(@Valid PostAuctionItemsRequest request) throws AuctionItemException;

	List<AuctionItemDto> getAuctionItems();

	AuctionItemDto getAuctionItem(String id);

	AuctionItemDto entityToDto(AuctionItem entity);
}
