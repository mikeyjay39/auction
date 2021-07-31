package com.example.auction.service;

import com.example.auction.dao.AuctionItemDao;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuctionItemsServiceImpl implements AuctionItemsService {

	private final AuctionItemDao auctionItemDao;
	private final ItemRepository itemRepository;
	private final BigDecimal zeroPrice = new BigDecimal("0");

	public AuctionItemsServiceImpl(AuctionItemDao auctionItemDao, ItemRepository itemRepository) {
		this.auctionItemDao = auctionItemDao;
		this.itemRepository = itemRepository;
	}

	@Override
	public PostAuctionItemsResponse postAuctionItems(PostAuctionItemsRequest request) throws
			AuctionItemException {

		validatePostAuctionItemsRequest(request);
		AuctionItem auctionItem = prepareAuctionItem(request);

		try {
			auctionItem = auctionItemDao.save(auctionItem);
		} catch (Exception e) {
			throw new AuctionItemException(e.getMessage());
		}
		return preparePostAuctionItemsResponse(auctionItem);
	}

	@Override
	public List<AuctionItemDto> getAuctionItems() {
		return getAllAuctionItemDtos();
	}

	@Override
	public AuctionItemDto getAuctionItem(String id) {
		Optional<AuctionItem> auctionItem = auctionItemDao.findOneFetchItem(new Long(id));

		if (auctionItem.isPresent()) {
			return entityToDto(auctionItem.get());
		} else {
			return null;
		}

	}

	private void validatePostAuctionItemsRequest(PostAuctionItemsRequest request) throws
			AuctionItemException {

		ItemDto itemDto = request.getItem();

		if (itemDto == null || itemDto.getItemId() == null) {
			throw new AuctionItemException("Request must include item");
		}

		if (request.getReservePrice() == null) {
			throw new AuctionItemException("Request must include a reserve price");
		}

		if (request.getReservePrice().compareTo(zeroPrice) <= 0) {
			throw new AuctionItemException("Request must include a positive number for reserve price");
		}
		String itemId = request.getItem().getItemId();
		Optional<Item> item = itemRepository.findById(new Long(itemId));

		if (!item.isPresent()) {
			throw new AuctionItemException(String.format("Item %s not found", itemId));
		}

		Optional<AuctionItem> auctionItem = auctionItemDao.findOneByItem(item.get());

		if (auctionItem.isPresent()) {
			throw new AuctionItemException(String.format("Auction already exists for item: %s", itemId));
		}
	}

	private AuctionItem prepareAuctionItem(PostAuctionItemsRequest request) {
		ItemDto itemDto = request.getItem();
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
		return auctionItemDao.findAllFetchItem().stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public AuctionItemDto entityToDto(AuctionItem entity) {
		AuctionItemDto dto = new AuctionItemDto();

		if (entity.getId() != null) {
			dto.setAuctionItemId(entity.getId().toString());
		}

		if (entity.getUser() != null) {
			dto.setBidderName(entity.getUser().getUsername());
		}

		dto.setCurrentBid(entity.getCurrentBid());
		dto.setReservePrice(entity.getReservePrice());
		dto.setMaxAutoBidAmount(entity.getMaxAutoBidAmount());
		ItemDto itemDto = new ItemDto();
		itemDto.setItemId(entity.getItem().getId().toString());
		itemDto.setDescription(entity.getItem().getDescription());
		dto.setItem(itemDto);

		return dto;
	}
}
