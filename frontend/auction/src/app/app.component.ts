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
  auctionItems: any = [];
  auctionItem: AuctionItemResponse = <AuctionItemResponse>{};

  constructor(private http: HttpClient) {
  }

  getAllAuctionItems() {
    this.auctionItem = <AuctionItemResponse>{};
    this.http.get("http://localhost:8080/api/v1/auctionItems").subscribe(
      (response) => {
        this.auctionItems = response;
      }
    );
  }

  getAuctionItems(id: string) {
    this.auctionItems = null;
    let requestUrl: String;
    requestUrl = "http://localhost:8080/api/v1/auctionItems/".concat(id);
    console.log(requestUrl);

    this.http.get(requestUrl.toString()).subscribe(
      (response) => {
        this.auctionItem = (<AuctionItemResponse>response);
      }
    );
  }
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
