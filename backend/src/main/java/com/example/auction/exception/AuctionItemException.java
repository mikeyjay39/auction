package com.example.auction.exception;

public class AuctionItemException extends Exception {

	public AuctionItemException() {
	}

	public AuctionItemException(String message) {
		super(message);
	}

	public AuctionItemException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuctionItemException(Throwable cause) {
		super(cause);
	}

	public AuctionItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
