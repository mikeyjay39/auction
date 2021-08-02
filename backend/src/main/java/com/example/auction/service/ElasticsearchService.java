package com.example.auction.service;

import com.example.auction.contants.ApiStatus;
import com.example.auction.dto.PostBidsRequest;

import java.util.concurrent.CompletableFuture;

public interface ElasticsearchService {

	CompletableFuture<ApiStatus> logPostBids(PostBidsRequest request);
}
