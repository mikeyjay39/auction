package com.example.auction.service;

import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostBidsRequest;
import com.example.auction.exception.PostBidsException;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {


	@Override
	public AuctionItemDto postBids(PostBidsRequest request) throws PostBidsException {
		return null;
	}
}
