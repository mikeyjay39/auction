package com.example.auction.dto;

import javax.validation.constraints.Pattern;

public class ItemDto {

	private String itemId;

	@Pattern(message="Only alphanumeric characters allowed for description", regexp = "[a-zA-Z0-9 ]+")
	private String description;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
