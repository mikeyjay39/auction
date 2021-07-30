package com.example.auction.service;

import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.Item;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.ItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.repository.AuctionItemRepository;
import com.example.auction.repository.ItemRepository;
import com.example.auction.test.PostgresFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

class AuctionItemsServiceImplTestIT extends PostgresFactory {

	@Autowired
	private AuctionItemsService auctionItemsService;

	@Autowired
	private AuctionItemRepository auctionItemRepository;

	@Autowired
	private ItemRepository itemRepository;

	private BigDecimal reservePrice = new BigDecimal("10450.00");

	@Test
	@Transactional
	void postAuctionItems() throws Exception {
		postAuctionItemsTest();
	}

	@Test
	@Transactional
	void getAuctionItems() throws Exception {
		getAuctionItemsTest();
	}

	@Test
	@Transactional
	void getAuctionItem() throws Exception {
		getAuctionItemTest();
	}

	private void postAuctionItemsTest() throws Exception {
		Item item = itemRepository.findAll().stream()
				.findFirst().get();
		PostAuctionItemsResponse response = createAuctionItem(item);
		Assert.assertNotNull("Response should not be null", response);
		Assert.assertTrue("Failed to add auctionItem",
				new Long(response.getAuctionItemId()) > 0L);
		AuctionItem auctionItem = auctionItemRepository.getById(new Long(response.getAuctionItemId()));
		Assert.assertTrue("Reserve price doesn't match",
				auctionItem.getReservePrice().compareTo(reservePrice) == 0);
		Assert.assertTrue("Current bid should be 0",
				auctionItem.getCurrentBid().compareTo(new BigDecimal("0")) == 0);
	}

	private void getAuctionItemsTest() throws Exception {
		List<Item> items = itemRepository.findAll();

		for (Item item : items) {
			createAuctionItem(item);
		}

		List<AuctionItemDto> auctionItems = auctionItemsService.getAuctionItems();
		Assert.assertFalse("No result returned", auctionItems.isEmpty());

		for (AuctionItemDto dto: auctionItems) {
			Assert.assertFalse("No id set", dto.getAuctionItemId() == null
					&& dto.getAuctionItemId().isEmpty());
			Assert.assertTrue("Reserve price doesn't match",
					dto.getReservePrice().compareTo(reservePrice) == 0);
			Assert.assertTrue("CurrentBid doesn't match",
					dto.getCurrentBid().compareTo(new BigDecimal("0")) == 0);
		}

	}

	private void getAuctionItemTest() throws Exception {
		Item item = itemRepository.findAll().stream()
				.findFirst().get();
		createAuctionItem(item);
		AuctionItem auctionItem = auctionItemRepository.findAll().stream()
						.findFirst().get();

		AuctionItemDto dto = auctionItemsService.getAuctionItem(auctionItem.getId().toString());
		Assert.assertFalse("No id set", dto.getAuctionItemId() == null
				&& dto.getAuctionItemId().isEmpty());
		Assert.assertTrue("Reserve price doesn't match",
				dto.getReservePrice().compareTo(auctionItem.getReservePrice()) == 0);
		Assert.assertTrue("CurrentBid doesn't match",
				dto.getCurrentBid().compareTo(auctionItem.getCurrentBid()) == 0);
	}

	private PostAuctionItemsResponse createAuctionItem(Item item) throws Exception {
		ItemDto itemDto = new ItemDto();
		itemDto.setItemId(item.getId().toString());
		itemDto.setDescription(item.getDescription());
		PostAuctionItemsRequest request = new PostAuctionItemsRequest();
		request.setItemDto(itemDto);
		request.setReservePrice(reservePrice);
		return auctionItemsService.postAuctionItems(request);
	}
}