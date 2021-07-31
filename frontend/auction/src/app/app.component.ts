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

  constructor(private http: HttpClient) {
  }

  getAllAuctionItems() {
    this.http.get("http://localhost:8080/api/v1/auctionItems").subscribe(
      (response) => {
        this.auctionItems = (<AuctionItemsResponse>response);
      }
    );
  }

  getAuctionItems(id: string) {
    let requestUrl: String;
    requestUrl = "http://localhost:8080/api/v1/auctionItems/".concat(id);
    console.log(requestUrl);

    this.http.get(requestUrl.toString()).subscribe(
      (response) => {
        this.auctionItems.status = (<AuctionItemResponse>response).status;
        this.auctionItems.result = [];
        this.auctionItems.result[0] = (<AuctionItemResponse>response).result;
      }
    );
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
