package com.example.auction.test;

import com.example.auction.domain.Item;
import com.example.auction.dto.ItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.service.AuctionItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestDataFactory {

	private BigDecimal reservePrice = new BigDecimal("10450.00");

	@Autowired
	private AuctionItemsService auctionItemsService;

	public PostAuctionItemsResponse createAuctionItem(Item item) throws Exception {
		ItemDto itemDto = new ItemDto();
		itemDto.setItemId(item.getId().toString());
		itemDto.setDescription(item.getDescription());
		PostAuctionItemsRequest request = new PostAuctionItemsRequest();
		request.setItem(itemDto);
		request.setReservePrice(reservePrice);
		return auctionItemsService.postAuctionItems(request);
	}
}
