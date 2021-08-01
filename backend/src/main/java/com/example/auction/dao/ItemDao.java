package com.example.auction.dao;

import com.example.auction.domain.Item;
import com.example.auction.repository.ItemRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ItemDao {

	private final ItemRepository itemRepository;

	public ItemDao(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Cacheable(value = "item")
	public Optional<Item> findById(Long id) {
		return itemRepository.findById(id);
	}

	@Cacheable(value = "item")
	public Item getById(Long id) {
		return itemRepository.getById(id);
	}
}
