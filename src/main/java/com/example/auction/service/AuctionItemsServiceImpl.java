package com.example.auction.service;

import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.Item;
import com.example.auction.dto.ItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.exception.AuctionItemException;
import com.example.auction.repository.AuctionItemRepository;
import com.example.auction.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuctionItemsServiceImpl implements AuctionItemsService {

	private final AuctionItemRepository auctionItemRepository;
	private final ItemRepository itemRepository;
	private final BigDecimal minReservePrice = new BigDecimal("0");

	public AuctionItemsServiceImpl(AuctionItemRepository auctionItemRepository, ItemRepository itemRepository) {
		this.auctionItemRepository = auctionItemRepository;
		this.itemRepository = itemRepository;
	}

	@Override
	public PostAuctionItemsResponse postAuctionItems(PostAuctionItemsRequest request) throws
			AuctionItemException {

		validatePostAuctionItemsRequest(request);
		AuctionItem auctionItem = prepareAuctionItem(request);
		auctionItem = auctionItemRepository.save(auctionItem);
		return preparePostAuctionItemsResponse(auctionItem);
	}

	private void validatePostAuctionItemsRequest(PostAuctionItemsRequest request) throws
			AuctionItemException {

		ItemDto itemDto = request.getItemDto();

		if (itemDto == null || itemDto.getId() == null) {
			throw new AuctionItemException("Request must include item");
		}

		if (request.getReservePrice() == null) {
			throw new AuctionItemException("Request must include a reserve price");
		}

		if (request.getReservePrice().compareTo(minReservePrice) <= 0) {
			throw new AuctionItemException("Request must include a positive number for reserve price");
		}
	}

	private AuctionItem prepareAuctionItem(PostAuctionItemsRequest request) {
		ItemDto itemDto = request.getItemDto();
		Item item = itemRepository.getById(itemDto.getId());
		AuctionItem auctionItem = new AuctionItem();
		auctionItem.setItem(item);
		auctionItem.setReservePrice(request.getReservePrice());
		return auctionItem;
	}

	private PostAuctionItemsResponse preparePostAuctionItemsResponse(AuctionItem auctionItem) {
		PostAuctionItemsResponse response = new PostAuctionItemsResponse();
		response.setAuctionItemId(auctionItem.getId().toString());
		return response;
	}
}
