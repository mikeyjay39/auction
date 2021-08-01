package com.example.auction.dto;

import java.math.BigDecimal;

public class PostAuctionItemsRequest {

	private BigDecimal reservePrice;

	private ItemDto item;

	public BigDecimal getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(BigDecimal reservePrice) {
		this.reservePrice = reservePrice;
	}

	public ItemDto getItem() {
		return item;
	}

	public void setItem(ItemDto item) {
		this.item = item;
	}
}
