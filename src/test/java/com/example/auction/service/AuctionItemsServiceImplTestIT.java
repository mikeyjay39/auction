package com.example.auction.service;

import com.example.auction.domain.Item;
import com.example.auction.dto.ItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.repository.ItemRepository;
import com.example.auction.test.PostgresFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

class AuctionItemsServiceImplTestIT extends PostgresFactory {

	@Autowired
	private AuctionItemsService auctionItemsService;

	@Autowired
	private ItemRepository itemRepository;

	@Test
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
		request.setReservePrice(new BigDecimal("10450.00"));
		PostAuctionItemsResponse response = auctionItemsService.postAuctionItems(request);
		Assert.assertNotNull("Response should not be null", response);
		Assert.assertTrue("Failed to add auctionItem",
				new Long(response.getAuctionItemId()) > 0L);
	}
}