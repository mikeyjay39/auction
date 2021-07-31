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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BidServiceImpl implements BidService {

	private final AuctionItemRepository auctionItemRepository;
	private final AuctionItemsService auctionItemsService;
	private final ElasticsearchService elasticsearchService;
	private final UserRepository userRepository;

	public BidServiceImpl(AuctionItemRepository auctionItemRepository,
						  AuctionItemsService auctionItemsService,
						  ElasticsearchService elasticsearchService,
						  UserRepository userRepository) {
		this.auctionItemRepository = auctionItemRepository;
		this.auctionItemsService = auctionItemsService;
		this.elasticsearchService = elasticsearchService;
		this.userRepository = userRepository;
	}

	@Override
	public ApiResponse<AuctionItemDto> postBids(PostBidsRequest request) throws PostBidsException {
		BigDecimal maxBidAmount = validateMaxAutoBidAmount(request);
		AuctionItem auctionItem = validateAuctionItemId(request);

		if (isReservePriceNotMet(maxBidAmount, auctionItem)) {
			return doReservePriceNotMet(maxBidAmount, auctionItem);
		}

		if (isMaxBidGreaterThanCurrentBid(maxBidAmount, auctionItem)) {
			return doNewHighBid(maxBidAmount, auctionItem, request);
		} else {
			return doOutbid(maxBidAmount, auctionItem);
		}
	}

	private BigDecimal validateMaxAutoBidAmount(PostBidsRequest request) throws PostBidsException {
		BigDecimal maxBidAmount = request.getMaxAutoBidAmount();
		if (maxBidAmount == null || maxBidAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new PostBidsException(String.format("Invalid bid amount of %s", maxBidAmount));
		}

		return maxBidAmount;
	}

	private AuctionItem validateAuctionItemId(PostBidsRequest request) throws PostBidsException {
		String auctionItemId = request.getAuctionItemId();

		if (auctionItemId == null || auctionItemId.isEmpty()) {
			throw new PostBidsException("Invalid request. AuctionItemId is missing");
		}

		Optional<AuctionItem> optionalAuctionItem = auctionItemRepository.findOneFetchItem(new Long(auctionItemId));

		if (!optionalAuctionItem.isPresent()) {
			throw new PostBidsException(String.format("No auction found with id = %s", auctionItemId));
		}

		return optionalAuctionItem.get();
	}

	private boolean isReservePriceNotMet(BigDecimal maxBidAmount, AuctionItem auctionItem) {
		return maxBidAmount.compareTo(auctionItem.getReservePrice()) < 0;
	}

	private ApiResponse<AuctionItemDto> doReservePriceNotMet(BigDecimal maxBidAmount,
															 AuctionItem auctionItem) {
		ApiResponse<AuctionItemDto> apiResponse = new ApiResponse<>();
		BigDecimal newCurrentBid = maxBidAmount.compareTo(auctionItem.getCurrentBid()) < 0 ?
				auctionItem.getCurrentBid() : maxBidAmount;
		auctionItem.setCurrentBid(newCurrentBid);
		auctionItemRepository.save(auctionItem);
		AuctionItemDto dto = auctionItemsService.entityToDto(auctionItem);
		apiResponse.setResult(dto);
		apiResponse.setStatus("Bid has not met the reserve price");
		return apiResponse;
	}

	private boolean isMaxBidGreaterThanCurrentBid(BigDecimal maxBidAmount, AuctionItem auctionItem) {
		return maxBidAmount.compareTo(auctionItem.getCurrentBid().add(BigDecimal.ONE)) >= 0;
	}

	private ApiResponse<AuctionItemDto> doNewHighBid(BigDecimal maxBidAmount,
													 AuctionItem auctionItem,
													 PostBidsRequest request) {

		ApiResponse<AuctionItemDto> apiResponse = new ApiResponse<>();
		BigDecimal currentMaxAutoBid = auctionItem.getMaxAutoBidAmount();
		BigDecimal newCurrentBid;

		if (isNewBidHigherThanMax(maxBidAmount, currentMaxAutoBid)) {
			newCurrentBid = currentMaxAutoBid.add(BigDecimal.ONE).max(auctionItem.getReservePrice());
			auctionItem.setMaxAutoBidAmount(maxBidAmount);
			User user = userRepository.findByUsername(request.getBidderName());
			auctionItem.setUser(user);
			apiResponse.setStatus(ApiStatus.SUCCESS.name());
		} else {
			newCurrentBid = maxBidAmount.add(BigDecimal.ONE);
			apiResponse.setStatus(ApiStatus.OUTBID.name());
		}

		auctionItem.setCurrentBid(newCurrentBid);
		auctionItemRepository.save(auctionItem);
		AuctionItemDto dto = auctionItemsService.entityToDto(auctionItem);
		apiResponse.setResult(dto);
		return apiResponse;
	}

	private boolean isNewBidHigherThanMax(BigDecimal maxBidAmount, BigDecimal currentMaxAutoBid) {
		return maxBidAmount.compareTo(currentMaxAutoBid.add(BigDecimal.ONE)) >= 0;
	}

	private ApiResponse<AuctionItemDto> doOutbid(BigDecimal maxBidAmount,
												 AuctionItem auctionItem) {
		ApiResponse<AuctionItemDto> apiResponse = new ApiResponse<>();
		auctionItem.setCurrentBid(maxBidAmount.max(auctionItem.getCurrentBid()));
		auctionItemRepository.save(auctionItem);
		AuctionItemDto dto = auctionItemsService.entityToDto(auctionItem);
		apiResponse.setResult(dto);
		apiResponse.setStatus(ApiStatus.OUTBID.name());
		return apiResponse;
	}
}
