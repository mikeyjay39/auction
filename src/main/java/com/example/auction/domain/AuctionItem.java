package com.example.auction.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "auction_item")
public class AuctionItem {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "reserve_price")
	private BigDecimal reservePrice;

	@Column(name = "current_bid")
	private BigDecimal currentBid;

	@Column(name = "max_auto_bid_amount")
	private BigDecimal maxAutoBidAmount;

	@OneToOne
	@Column(name = "item")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
