package com.example.auction.dto;

import java.math.BigDecimal;

public class AuctionItemDto {

	private String auctionItemId;

	private BigDecimal reservePrice;

	private BigDecimal currentBid;

	private BigDecimal maxAutoBidAmount;

	private ItemDto item;

	private String bidderName;

	public String getAuctionItemId() {
		return auctionItemId;
	}

	public void setAuctionItemId(String auctionItemId) {
		this.auctionItemId = auctionItemId;
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

	public ItemDto getItem() {
		return item;
	}

	public void setItem(ItemDto item) {
		this.item = item;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}
}
