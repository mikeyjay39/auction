package com.example.auction.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "reserve_price")
	private BigDecimal reservePrice = BigDecimal.ZERO;

	@Column(name = "current_bid")
	private BigDecimal currentBid = BigDecimal.ZERO;

	@Column(name = "max_auto_bid_amount")
	private BigDecimal maxAutoBidAmount = BigDecimal.ZERO;

	@OneToOne
	@JoinColumn(name = "item")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(BigDecimal reservePrice) {
		this.reservePrice = reservePrice;
	}

	public BigDecimal getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(BigDecimal currentBid) {
		this.currentBid = currentBid;
	}

	public BigDecimal getMaxAutoBidAmount() {
		return maxAutoBidAmount;
	}

	public void setMaxAutoBidAmount(BigDecimal maxAutoBidAmount) {
		this.maxAutoBidAmount = maxAutoBidAmount;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
