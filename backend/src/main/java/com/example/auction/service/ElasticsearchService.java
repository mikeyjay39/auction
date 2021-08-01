package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.dto.PostBidsRequest;

public interface ElasticsearchService {

	ApiStatus logPostBids(PostBidsRequest request);
}
