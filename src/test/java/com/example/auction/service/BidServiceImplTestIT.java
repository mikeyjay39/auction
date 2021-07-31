package com.example.auction.service;

import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.Item;
import com.example.auction.dto.ApiResponse;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.repository.AuctionItemRepository;
import com.example.auction.repository.ItemRepository;
import com.example.auction.test.PostgresFactory;
import com.example.auction.test.TestDataFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

class BidServiceImplTestIT extends PostgresFactory {

	@Autowired
	private AuctionItemRepository auctionItemRepository;

	@Autowired
	private BidService bidService;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private TestDataFactory dataFactory;

	@Test
	@Transactional
	public void postBids() throws Exception {
		postBidsTest();
	}

	private void postBidsTest() throws Exception {
		Item item = itemRepository.findAll().stream().findFirst().get();
		AuctionItem auctionItem = auctionItemRepository
				.getById(new Long(dataFactory.createAuctionItem(item).getAuctionItemId()));
		BigDecimal originalBid = auctionItem.getCurrentBid();
		PostBidsRequest request = new PostBidsRequest();
		request.setBidderName("John Lennon");
		request.setAuctionItemId(auctionItem.getId().toString());
		BigDecimal maxAutoBid = auctionItem.getReservePrice();
		BigDecimal newBid = maxAutoBid.add(maxAutoBid);
		request.setMaxAutoBidAmount(newBid);
		ApiResponse<AuctionItemDto> response = bidService.postBids(request);
		Assert.assertTrue("currentBid was not updated",
				response.getResult().getCurrentBid().compareTo(originalBid) > 0);
	}
}