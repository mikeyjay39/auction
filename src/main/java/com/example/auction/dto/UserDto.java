package com.example.auction.dto;

import java.util.List;

public class UserDto {

	private Long id;

	private String username;

	private List<AuctionItemDto> auctionItemDtoList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<AuctionItemDto> getAuctionItemDtoList() {
		return auctionItemDtoList;
	}

	public void setAuctionItemDtoList(List<AuctionItemDto> auctionItemDtoList) {
		this.auctionItemDtoList = auctionItemDtoList;
	}
}
