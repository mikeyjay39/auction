package com.example.auction.dao;

import com.example.auction.domain.AuctionItem;
import com.example.auction.domain.Item;
import com.example.auction.repository.AuctionItemRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Component
@Validated
public class AuctionItemDao {

	private final AuctionItemRepository auctionItemRepository;

	public AuctionItemDao(AuctionItemRepository auctionItemRepository) {
		this.auctionItemRepository = auctionItemRepository;
	}


	@CachePut(value = "auctionItem", key = "#auctionItem.id")
	@Caching(evict = {@CacheEvict(value = "auctionItems", allEntries = true),
			@CacheEvict(value = "itemAuctionItem", key = "#auctionItem.item.id")
	})
	public AuctionItem save(AuctionItem auctionItem) {
		return auctionItemRepository.save(auctionItem);
	}

	@Cacheable(value = "itemAuctionItem", key = "#item.id")
	public Optional<AuctionItem> findOneByItem(Item item) {
		return auctionItemRepository.findOneByItem(item);
	}

	@Cacheable(value = "auctionItem", key = "#id")
	public Optional<AuctionItem> findOneFetchItem(Long id) {
		return auctionItemRepository.findOneFetchItem(id);
	}

	@Cacheable(value = "auctionItems")
	public List<AuctionItem> findAllFetchItem() {
		return auctionItemRepository.findAll();
	}
}
