package com.example.auction.service;

import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.Item;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.ItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.exception.AuctionItemException;
import com.example.auction.repository.AuctionItemRepository;
import com.example.auction.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionItemsServiceImpl implements AuctionItemsService {

	private final AuctionItemRepository auctionItemRepository;
	private final ItemRepository itemRepository;
	private final BigDecimal zeroPrice = new BigDecimal("0");

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

	@Override
	public List<AuctionItemDto> getAuctionItems() {
		return getAllAuctionItemDtos();
	}

	@Override
	public AuctionItemDto getAuctionItem(String id) {
		AuctionItem auctionItem = auctionItemRepository.findOneFetchItem(new Long(id));
		return entityToDto(auctionItem);
	}

	private void validatePostAuctionItemsRequest(PostAuctionItemsRequest request) throws
			AuctionItemException {

		ItemDto itemDto = request.getItemDto();

		if (itemDto == null || itemDto.getItemId() == null) {
			throw new AuctionItemException("Request must include item");
		}

		if (request.getReservePrice() == null) {
			throw new AuctionItemException("Request must include a reserve price");
		}

		if (request.getReservePrice().compareTo(zeroPrice) <= 0) {
			throw new AuctionItemException("Request must include a positive number for reserve price");
		}
	}

	private AuctionItem prepareAuctionItem(PostAuctionItemsRequest request) {
		ItemDto itemDto = request.getItemDto();
		Item item = itemRepository.getById(new Long(itemDto.getItemId()));
		AuctionItem auctionItem = new AuctionItem();
		auctionItem.setItem(item);
		auctionItem.setReservePrice(request.getReservePrice());
		auctionItem.setCurrentBid(zeroPrice);
		return auctionItem;
	}

	private PostAuctionItemsResponse preparePostAuctionItemsResponse(AuctionItem auctionItem) {
		PostAuctionItemsResponse response = new PostAuctionItemsResponse();
		response.setAuctionItemId(auctionItem.getId().toString());
		return response;
	}

	private List<AuctionItemDto> getAllAuctionItemDtos() {
		return auctionItemRepository.findAllFetchItem().stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public AuctionItemDto entityToDto(AuctionItem entity) {
		AuctionItemDto dto = new AuctionItemDto();

		if (entity.getId() != null) {
			dto.setAuctionItemId(entity.getId().toString());
		}

		dto.setCurrentBid(entity.getCurrentBid());
		dto.setReservePrice(entity.getReservePrice());
		ItemDto itemDto = new ItemDto();
		itemDto.setItemId(entity.getItem().getId().toString());
		itemDto.setDescription(entity.getItem().getDescription());
		dto.setItem(itemDto);

		return dto;
	}
}
