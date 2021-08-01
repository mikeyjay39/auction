package com.example.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ElasticsearchBidDto {

	private String auctionItemId;

	private String bidderName;

	private BigDecimal maxAutoBidAmount;

	private LocalDateTime timestamp;

	public String getAuctionItemId() {
		return auctionItemId;
	}

	public void setAuctionItemId(String auctionItemId) {
		this.auctionItemId = auctionItemId;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public BigDecimal getMaxAutoBidAmount() {
		return maxAutoBidAmount;
	}

	public void setMaxAutoBidAmount(BigDecimal maxAutoBidAmount) {
		this.maxAutoBidAmount = maxAutoBidAmount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
