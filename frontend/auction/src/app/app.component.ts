import {Component, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
@Injectable()
export class AppComponent {
  title = 'auction';
  auctionItems: AuctionItemsResponse = <AuctionItemsResponse>{};
  postAuctionItemsRequest: PostAuctionItemsRequest = <PostAuctionItemsRequest>{};
  postAuctionItemsReservePrice: number = 1;
  postAuctionItemsReserveItemId: string = '';
  postAuctionItemsReserveItemDescription: string = '';
  postAuctionItemsResponse: PostAuctionItemsResponse = <PostAuctionItemsResponse>{};
  postBidsRequest: PostBidsRequest = <PostBidsRequest>{};
  postBidsResponse: PostBidsResponse = <PostBidsResponse>{};
  baseUrl: string = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient) {
  }

  getAllAuctionItems() {
    this.http.get(this.baseUrl.concat("auctionItems")).subscribe(
      (response) => {
        this.auctionItems = (<AuctionItemsResponse>response);
      }
    );
  }

  getAuctionItems(id: string) {
    let requestUrl: String;
    requestUrl = this.baseUrl.concat("auctionItems/").concat(id);
    console.log(requestUrl);

    this.http.get(requestUrl.toString()).subscribe(
      (response) => {
        this.auctionItems.status = (<AuctionItemResponse>response).status;
        this.auctionItems.result = [];
        this.auctionItems.result[0] = (<AuctionItemResponse>response).result;
      }
    );
  }

  postAuctionItems() {
    console.log(this.postAuctionItemsReservePrice);
    let request = {
      reservePrice: this.postAuctionItemsReservePrice,
      item: {
        itemId: this.postAuctionItemsReserveItemId,
        description: this.postAuctionItemsReserveItemDescription
      }
    }
    this.http.post(this.baseUrl.concat("auctionItems"), request)
      .subscribe(
        (response) => {
          this.postAuctionItemsResponse.status = (<AuctionItemResponse>response).status;
          this.postAuctionItemsResponse.result = (<AuctionItemResponse>response).result;
        }
      )
  }

  postBid() {
    console.log(this.postBidsRequest.auctionItemId);
    console.log(this.postBidsRequest.maxAutoBidAmount);
    console.log(this.postBidsRequest.bidderName);

    this.http.post(this.baseUrl.concat("bids"), this.postBidsRequest)
      .subscribe(
        (response) => {
          this.postBidsResponse.status = (<PostBidsResponse>response).status;
          this.postBidsResponse.result = (<PostBidsResponse>response).result;
        }
      )
  }
}

interface AuctionItemsResponse {
  status: string
  result: AuctionItem[]
}

interface AuctionItemResponse {
  status: string
  result: AuctionItem
}

interface AuctionItem {
  auctionItemId: string
  reservePrice: number
  currentBid: number
  maxAutoBidAmount: number
  item: Item
  bidderName: string
}

interface Item {
  itemId: string
  description: string
}

interface PostAuctionItemsRequest {
  reservePrice: number
  item: Item
}

interface PostAuctionItemsResponse {
  status: string
  result: AuctionItemId
}

interface AuctionItemId {
  auctionItemId: string
}

interface PostBidsRequest {
  auctionItemId: string
  maxAutoBidAmount: number
  bidderName: string
}

interface PostBidsResponse {
  status: string
  result: AuctionItemId
}
