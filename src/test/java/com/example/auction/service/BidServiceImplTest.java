package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.Item;
import com.example.auction.domain.User;
import com.example.auction.dto.ApiResponse;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.exception.PostBidsException;
import com.example.auction.repository.AuctionItemRepository;
import com.example.auction.repository.UserRepository;
import org.junit.Assert;
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
	BigDecimal previousCurrentBid;
	BigDecimal previousMaxAutoBid;

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

		previousCurrentBid = new BigDecimal(auctionItem.getCurrentBid().toString());
		previousMaxAutoBid = new BigDecimal(auctionItem.getMaxAutoBidAmount().toString());
	}

	@Test
	void postBidsOutbidded() throws Exception {
		postBidsOutbiddedTest();
	}

	@Test
	void postBidsNewHighBid() throws Exception {
		postBidsNewHighBidTest();
	}

	@Test
	void postBidsInvalidBidMet() throws Exception {
		postBidsInvalidBidTest();
	}

	@Test
	void postBidsReservePriceNotMet() throws Exception {
		postBidsReservePriceNotMetTest();
	}

	private void postBidsOutbiddedTest() throws Exception {
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

	private void postBidsNewHighBidTest() throws Exception {
		PostBidsRequest postBidsRequest = new PostBidsRequest();
		postBidsRequest.setAuctionItemId(auctionItem.getId().toString());
		postBidsRequest.setBidderName(selfUser.getUsername());
		postBidsRequest.setMaxAutoBidAmount(previousMaxAutoBid.add(previousMaxAutoBid));
		ApiResponse<AuctionItemDto> response = bidService.postBids(postBidsRequest);
		Assert.assertEquals("Status should be SUCCESS", ApiStatus.SUCCESS.toString(), response.getStatus());
		Assert.assertEquals("Current bidder should have changed",
				selfUser.getUsername(),
				response.getResult().getBidderName());
		Assert.assertEquals("Current bid should be 1 higher than previous max bid",
				previousMaxAutoBid.add(BigDecimal.ONE), response.getResult().getCurrentBid());
		Assert.assertEquals("Max auto bid not correctly set",
				previousMaxAutoBid.add(previousMaxAutoBid),
				response.getResult().getMaxAutoBidAmount());
	}

	private void postBidsInvalidBidTest() throws Exception {
		PostBidsRequest postBidsRequest = new PostBidsRequest();
		postBidsRequest.setAuctionItemId(auctionItem.getId().toString());
		postBidsRequest.setBidderName(selfUser.getUsername());
		postBidsRequest.setMaxAutoBidAmount(BigDecimal.ZERO.subtract(BigDecimal.ONE));
		Assert.assertThrows(PostBidsException.class, () -> bidService.postBids(postBidsRequest));
	}

	private void postBidsReservePriceNotMetTest() throws Exception {
		PostBidsRequest postBidsRequest = new PostBidsRequest();
		postBidsRequest.setAuctionItemId(auctionItem.getId().toString());
		postBidsRequest.setBidderName(selfUser.getUsername());
		postBidsRequest.setMaxAutoBidAmount(BigDecimal.ONE);
		ApiResponse<AuctionItemDto> response = bidService.postBids(postBidsRequest);
		Assert.assertEquals("Should return reserve price not met status",
				"Bid has not met the reserve price",
				response.getStatus());
	}
}