package com.example.auction.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
public class AuctionItem {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	private BigDecimal reservePrice;

	private BigDecimal currentBid;

	private BigDecimal maxAutoBidAmount;

	@OneToOne
	private Item item;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

}
