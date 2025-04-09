import { inject, Injectable } from '@angular/core';
import { EnvironmentStore } from '../../stores';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Apis, ItemResponse, Page, Portfolio } from '../../models';
import { PortfolioSummary } from '../../models/portfolio/portfolio-summary.model';

@Injectable({
  providedIn: 'root',
})
export class PortfolioService {
  readonly env = inject(EnvironmentStore);
  readonly http = inject(HttpClient);

  getPortfolioSummary(): Observable<ItemResponse<PortfolioSummary>> {
    const url = this.env.apiUrl() + Apis.Portfolio + Page.PortfolioSummary;
    return this.http.get<ItemResponse<PortfolioSummary>>(url);
  }

  resetPortfolio(): Observable<ItemResponse<string>> {
    const url = this.env.apiUrl() + Apis.Portfolio + Page.ResetHistory;
    return this.http.get<ItemResponse<string>>(url);
  }
}
