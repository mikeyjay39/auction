import {Component, Injectable} from '@angular/core';
import {
  HttpClient,
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpRequest,
  HttpXsrfTokenExtractor
} from "@angular/common/http";
import {Observable} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
@Injectable()
export class AppComponent {
  title = 'auction';
  authenticated = false;
  noLoginError: boolean = true;
  username: string = '';
  password: string = '';
  bidderName: string = '';
  bidderNames = [
    "John Lennon",
    "Paul McCartney",
    "George Harrison",
    "Ringo Starr"
  ]
  auctionItems: AuctionItemsResponse = <AuctionItemsResponse>{};
  postAuctionItemsRequest: PostAuctionItemsRequest = <PostAuctionItemsRequest>{};
  postAuctionItemsReservePrice: number = 1;
  postAuctionItemsReserveItemId: string = '';
  postAuctionItemsReserveItemDescription: string = '';
  postAuctionItemsResponse: PostAuctionItemsResponse = <PostAuctionItemsResponse>{};
  postBidsRequest: PostBidsRequest = <PostBidsRequest>{};
  postBidsResponse: PostBidsResponse = <PostBidsResponse>{};
  baseUrl: string = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient, private tokenExtractor: HttpXsrfTokenExtractor) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const headerName = 'XSRF-TOKEN';
    const respHeaderName = 'X-XSRF-TOKEN';
    let token = this.tokenExtractor.getToken() as string;
    if (token !== null && !req.headers.has(headerName)) {
      req = req.clone({headers: req.headers.set(respHeaderName, token)});
    }
    return next.handle(req);
  }

  getAllAuctionItems() {
    this.http.get(this.baseUrl.concat("auctionItems"), {withCredentials: true}).subscribe(
      (response) => {
        this.auctionItems = (<AuctionItemsResponse>response);
      }
    );
  }

  getAuctionItems(id: string) {
    let requestUrl: String;
    requestUrl = this.baseUrl.concat("auctionItems/").concat(id);
    console.log(requestUrl);

    this.http.get(requestUrl.toString(), {withCredentials: true}).subscribe(
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
    this.http.post(this.baseUrl.concat("auctionItems"), request, {withCredentials: true})
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

    this.http.post(this.baseUrl.concat("bids"), this.postBidsRequest, {withCredentials: true})
      .subscribe(
        (response) => {
          this.postBidsResponse.status = (<PostBidsResponse>response).status;
          this.postBidsResponse.result = (<PostBidsResponse>response).result;
        }
      )
  }

  bidderChanged($event: Event) {
    console.log("Bidder name set to ".concat(this.bidderName));
    this.postBidsRequest.bidderName = this.bidderName;
  }

  isAuthenticated() {
    return this.authenticated;
  }

  isNoLoginError() {
    return this.noLoginError;
  }

  login() {
    this.authenticate(this.username, this.password);
    return false;
  }

  authenticate(username: string, password: string) {
    const headers = new HttpHeaders({
      authorization: 'Basic ' + btoa(username.concat(':').concat(password))
    });

    this.http.get(this.baseUrl.concat("login"), {headers: headers, observe: "response"}).subscribe(response => {
        console.log(response.status);
        if (response) {
          this.authenticated = true;
          this.noLoginError = true;
        } else {
          console.log("Failed to log in");
          this.authenticated = false;
          this.noLoginError = false;
        }
      },
      (error) => {
        console.error("Failed to log in")
        this.authenticated = false;
        this.noLoginError = false;
      })

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
