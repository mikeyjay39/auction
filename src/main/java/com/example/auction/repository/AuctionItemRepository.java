package com.example.auction.repository;

import com.example.auction.domain.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {

	@Query("select ai from AuctionItem ai join fetch ai.item")
	List<AuctionItem> findAllFetchItem();

	@Query("select ai from AuctionItem ai join fetch ai.item where ai.id = :id")
	AuctionItem findOneFetchItem(Long id);
}
