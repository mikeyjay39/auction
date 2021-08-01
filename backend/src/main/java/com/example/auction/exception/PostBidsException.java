package com.example.auction.exception;

public class PostBidsException extends Exception {

	public PostBidsException() {
	}

	public PostBidsException(String message) {
		super(message);
	}

	public PostBidsException(String message, Throwable cause) {
		super(message, cause);
	}

	public PostBidsException(Throwable cause) {
		super(cause);
	}

	public PostBidsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
