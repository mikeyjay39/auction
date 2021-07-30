package com.example.auction.repository;

import com.example.auction.domain.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {
}
