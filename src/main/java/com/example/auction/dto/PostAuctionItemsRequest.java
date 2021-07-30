package com.example.auction.dto;

import java.math.BigDecimal;

public class PostAuctionItemsRequest {

	private BigDecimal reservePrice;

	private ItemDto itemDto;

	public BigDecimal getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(BigDecimal reservePrice) {
		this.reservePrice = reservePrice;
	}

	public ItemDto getItemDto() {
		return itemDto;
	}

	public void setItemDto(ItemDto itemDto) {
		this.itemDto = itemDto;
	}
}
