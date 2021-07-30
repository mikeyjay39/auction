package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.User;
import com.example.auction.dto.ApiResponse;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.exception.PostBidsException;
import com.example.auction.repository.AuctionItemRepository;
import com.example.auction.repository.UserRepository;
import liquibase.pro.packaged.A;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BidServiceImpl implements BidService {

	private final AuctionItemRepository auctionItemRepository;
	private final AuctionItemsService auctionItemsService;
	private final UserRepository userRepository;

	public BidServiceImpl(AuctionItemRepository auctionItemRepository, AuctionItemsService auctionItemsService, UserRepository userRepository) {
		this.auctionItemRepository = auctionItemRepository;
		this.auctionItemsService = auctionItemsService;
		this.userRepository = userRepository;
	}

	// TODO refactor
	@Override
	public ApiResponse<AuctionItemDto> postBids(PostBidsRequest request) throws PostBidsException {

		BigDecimal maxBidAmount = request.getMaxAutoBidAmount();
		ApiResponse apiResponse = new ApiResponse();

		if (maxBidAmount == null || maxBidAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new PostBidsException(String.format("Invalid bid amount of %s", maxBidAmount));
		}

		String auctionItemId = request.getAuctionItemId();

		if (auctionItemId == null || auctionItemId.isEmpty()) {
			throw new PostBidsException("Invalid request. AuctionItemId is missing");
		}

		AuctionItem auctionItem = auctionItemRepository.getById(new Long(auctionItemId));

		// reserve price not met
		if (maxBidAmount.compareTo(auctionItem.getReservePrice()) < 0) {
			BigDecimal newCurrentBid = maxBidAmount.compareTo(auctionItem.getCurrentBid()) < 0 ?
					auctionItem.getCurrentBid() : maxBidAmount;
			auctionItem.setCurrentBid(newCurrentBid);
			auctionItemRepository.save(auctionItem);
			AuctionItemDto dto = auctionItemsService.entityToDto(auctionItem);
			apiResponse.setResult(dto);
			apiResponse.setStatus("Bid has not met the reserve price");
			return apiResponse;
		}

		// new max bid is greater than current bid
		if (maxBidAmount.compareTo(auctionItem.getCurrentBid().add(BigDecimal.ONE)) >= 0) {

			BigDecimal currentMaxAutoBid = auctionItem.getMaxAutoBidAmount();
			BigDecimal newCurrentBid;

			if (maxBidAmount.compareTo(currentMaxAutoBid.add(BigDecimal.ONE)) >= 0) {
				// new bid is higher than max
				newCurrentBid = currentMaxAutoBid.add(BigDecimal.ONE);
				auctionItem.setMaxAutoBidAmount(maxBidAmount);
				User user = userRepository.findByUsername(request.getBidderName());
				auctionItem.setUser(user);
				apiResponse.setStatus(ApiStatus.SUCCESS.name());
			} else {
				// current bidder is still highest bidder but current bid increases
				newCurrentBid = maxBidAmount.add(BigDecimal.ONE);
				apiResponse.setStatus(ApiStatus.OUTBID.name());
			}

			auctionItem.setCurrentBid(newCurrentBid);
			auctionItemRepository.save(auctionItem);
			AuctionItemDto dto = auctionItemsService.entityToDto(auctionItem);
			apiResponse.setResult(dto);
			return apiResponse;
		} else {
			// outbidded
			auctionItem.setCurrentBid(maxBidAmount);
			auctionItemRepository.save(auctionItem);
			AuctionItemDto dto = auctionItemsService.entityToDto(auctionItem);
			apiResponse.setResult(dto);
			apiResponse.setStatus(ApiStatus.OUTBID.name());
			return apiResponse;
		}

	}
}
