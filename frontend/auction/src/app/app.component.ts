import {Component, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
@Injectable()
export class AppComponent {
  title = 'auction';
  auctionItems: any = [];
  constructor(private http: HttpClient) { }

  getAllAuctionItems() {
    this.http.get("http://localhost:8080/api/v1/auctionItems").subscribe(
      (response) => {
        this.auctionItems = response;
      }
    );
  }
}
