package com.example.auction.repository;

import com.example.auction.domain.Item;
import com.example.auction.test.PostgresFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ItemRepositoryTestIT extends PostgresFactory {

	@Autowired
	private ItemRepository itemRepository;

	@Test
	public void findAll() {
		findAllTest();
	}

	private void findAllTest() {
		List<Item> items = itemRepository.findAll();
		Assert.assertFalse(items.isEmpty());
	}

}