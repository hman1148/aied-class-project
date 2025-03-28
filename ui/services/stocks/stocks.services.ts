import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import {
  Apis,
  ItemResponse,
  ItemsResponse,
  Page,
  Stock,
  StockPurchaseRequest,
} from '../../models';
import { EnvironmentStore } from '../../stores';

@Injectable({
  providedIn: 'root',
})
export class StockService {
  readonly env = inject(EnvironmentStore);
  readonly http = inject(HttpClient);

  getStocks(): Observable<ItemsResponse<Stock>> {
    const url = this.env.apiUrl() + Apis.Stocks + '/' + Page.GetStocks;
    return this.http.get<ItemsResponse<Stock>>(url);
  }

  buyStock(
    stockPurchaseRequest: StockPurchaseRequest
  ): Observable<ItemResponse<Stock>> {
    const url = this.env.apiUrl() + Apis.Stocks + '/' + Page.BuyStocks;
    return this.http.post<ItemResponse<Stock>>(url, stockPurchaseRequest);
  }
}
