package com.example.auction.service;

import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.Item;
import com.example.auction.dto.ItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.repository.AuctionItemRepository;
import com.example.auction.repository.ItemRepository;
import com.example.auction.test.PostgresFactory;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

class AuctionItemsServiceImplTestIT extends PostgresFactory {

	@Autowired
	private AuctionItemsService auctionItemsService;

	@Autowired
	private AuctionItemRepository auctionItemRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	@Transactional
	void postAuctionItems() throws Exception {
		postAuctionItemsTest();
	}

	private void postAuctionItemsTest() throws Exception {
		Item item = itemRepository.findAll().stream()
				.findFirst().get();
		ItemDto itemDto = new ItemDto();
		itemDto.setId(item.getId());
		itemDto.setDescription(item.getDescription());
		PostAuctionItemsRequest request = new PostAuctionItemsRequest();
		request.setItemDto(itemDto);
		BigDecimal reservePrice = new BigDecimal("10450.00");
		request.setReservePrice(reservePrice);
		PostAuctionItemsResponse response = auctionItemsService.postAuctionItems(request);
		Assert.assertNotNull("Response should not be null", response);
		Assert.assertTrue("Failed to add auctionItem",
				new Long(response.getAuctionItemId()) > 0L);
		AuctionItem auctionItem = auctionItemRepository.getById(new Long(response.getAuctionItemId()));
		Assert.assertTrue("Reserve price doesn't match",
				auctionItem.getReservePrice().compareTo(reservePrice) == 0);
		Assert.assertTrue("Current bid should be 0",
				auctionItem.getCurrentBid().compareTo(new BigDecimal("0")) == 0);
	}
}