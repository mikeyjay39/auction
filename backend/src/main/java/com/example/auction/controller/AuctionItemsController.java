package com.example.auction.controller;

import com.example.auction.contants.ApiStatus;
import com.example.auction.dto.ApiResponse;
import com.example.auction.dto.AuctionItemDto;
import com.example.auction.dto.PostAuctionItemsRequest;
import com.example.auction.dto.PostAuctionItemsResponse;
import com.example.auction.exception.AuctionItemException;
import com.example.auction.service.AuctionItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auctionItems")
public class AuctionItemsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuctionItemsController.class);

	private final AuctionItemsService auctionItemsService;

	public AuctionItemsController(AuctionItemsService auctionItemsService) {
		this.auctionItemsService = auctionItemsService;
	}

	@PostMapping
	public ApiResponse<PostAuctionItemsResponse> postAuctionItems(@RequestBody PostAuctionItemsRequest request) {
		return doPostAuctionItems(request);
	}

	@GetMapping
	public ApiResponse<List<AuctionItemDto>> getAuctionItems() {
		return doGetAllAuctionItems();
	}

	@GetMapping("/{auctionItemId}")
	public ApiResponse<AuctionItemDto> getAuctionItems(@PathVariable("auctionItemId") String auctionItemId) {
		return doGetAuctionItems(auctionItemId);
	}

	private ApiResponse<PostAuctionItemsResponse> doPostAuctionItems(PostAuctionItemsRequest request) {
		LOGGER.trace("Request received: {}", request);
		ApiResponse<PostAuctionItemsResponse> apiResponse = new ApiResponse<>();

		try {
			PostAuctionItemsResponse response = auctionItemsService.postAuctionItems(request);
			apiResponse.setResult(response);
			apiResponse.setStatus(ApiStatus.SUCCESS.name());
			LOGGER.trace("Request succeeded");
		} catch (AuctionItemException e) {
			LOGGER.error(e.getMessage());
			apiResponse.setStatus(e.getMessage());
		} catch (ValidationException v) {
			LOGGER.error(v.getMessage());
			Optional<ConstraintViolation<?>> constraintViolationOptional = ((ConstraintViolationException) v)
					.getConstraintViolations().stream()
					.findFirst();

			String errMsg = "";

			if (constraintViolationOptional.isPresent()) {
				errMsg = constraintViolationOptional.get().getMessage();
			} else {
				errMsg = v.getMessage();
			}

			apiResponse.setStatus(errMsg);
		}

		return apiResponse;
	}

	private ApiResponse<List<AuctionItemDto>> doGetAllAuctionItems() {
		LOGGER.trace("doGetAuctionItems");
		ApiResponse<List<AuctionItemDto>> apiResponse = new ApiResponse<>();
		apiResponse.setResult(auctionItemsService.getAuctionItems());
		apiResponse.setStatus(ApiStatus.SUCCESS.name());
		return apiResponse;
	}

	private ApiResponse<AuctionItemDto> doGetAuctionItems(String auctionItemId) {
		LOGGER.trace("doGetAuctionItems");
		ApiResponse<AuctionItemDto> apiResponse = new ApiResponse<>();
		apiResponse.setResult(auctionItemsService.getAuctionItem(auctionItemId));

		if (apiResponse.getResult() == null) {
			apiResponse.setStatus(ApiStatus.NOTFOUND.name());
		} else {
			apiResponse.setStatus(ApiStatus.SUCCESS.name());
		}

		return apiResponse;
	}

}
