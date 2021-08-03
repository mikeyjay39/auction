package com.example.auction.dto;

import javax.validation.Valid;
import java.math.BigDecimal;

public class PostAuctionItemsRequest {

	private BigDecimal reservePrice;

	@Valid
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
