package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.Item;
import com.example.auction.domain.User;
import com.example.auction.dto.ApiResponse;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.repository.AuctionItemRepository;
import com.example.auction.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;

class BidServiceImplTest {

	private BidServiceImpl bidService;

	@Mock
	private AuctionItemRepository auctionItemRepository = Mockito.mock(AuctionItemRepository.class);

	private AuctionItemsService auctionItemsService = new AuctionItemsServiceImpl(null, null);

	@Mock
	private UserRepository userRepository = Mockito.mock(UserRepository.class);

	User selfUser;
	User user;
	AuctionItem auctionItem;

	@BeforeEach
	void setUp() {
		bidService = new BidServiceImpl(auctionItemRepository, auctionItemsService, userRepository);
		auctionItem = new AuctionItem();
		auctionItem.setId(1L);
		Item item = new Item();
		item.setDescription("mock description");
		item.setId(1L);
		auctionItem.setItem(item);
		user = new User();
		user.setId(1L);
		user.setUsername("ABC Dealership");
		auctionItem.setUser(user);
		auctionItem.setReservePrice(new BigDecimal("10450.00"));
		BigDecimal currentBid = new BigDecimal("11000.00");
		auctionItem.setCurrentBid(currentBid);
		auctionItem.setMaxAutoBidAmount(currentBid.add(currentBid));

		Mockito.doReturn(auctionItem)
				.when(auctionItemRepository)
				.findOneFetchItem(Mockito.anyLong());

		selfUser = new User();
		selfUser.setId(2L);
		selfUser.setUsername("John Lennon");

		Mockito.doReturn(selfUser)
				.when(userRepository)
				.findByUsername(Mockito.anyString());
	}

	@Test
	void postBidsOutbidded() throws Exception {
		BigDecimal previousCurrentBid = new BigDecimal(auctionItem.getCurrentBid().toString());
		PostBidsRequest postBidsRequest = new PostBidsRequest();
		postBidsRequest.setAuctionItemId(auctionItem.getId().toString());
		postBidsRequest.setBidderName(selfUser.getUsername());
		postBidsRequest.setMaxAutoBidAmount(auctionItem.getMaxAutoBidAmount().subtract(BigDecimal.ONE));
		ApiResponse<AuctionItemDto> response = bidService.postBids(postBidsRequest);
		Assert.assertEquals("Status should be Outbid", ApiStatus.OUTBID.toString(), response.getStatus());
		Assert.assertEquals("Current bidder should not have changed",
				user.getUsername(),
				response.getResult().getBidderName());
		Assert.assertNotEquals("Current bid did not change", previousCurrentBid, response.getResult().getCurrentBid());
	}
}