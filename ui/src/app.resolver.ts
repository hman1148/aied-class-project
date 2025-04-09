import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { StockStore } from '../stores/stocks/stock.store';
import { PortfolioStore } from '../stores';

export const stockResolver: ResolveFn<Promise<boolean>> = async (...args) => {
  const stockStore = inject(StockStore);

  if (!stockStore.isLoading() && stockStore.isEntitiesLoaded()) {
    return true;
  } else {
    return stockStore.resolveStocks();
  }
};

export const portfolioResolver: ResolveFn<Promise<boolean>> = async (
  ...args
) => {
  const portfolioStore = inject(PortfolioStore);

  if (!portfolioStore.isLoading() && portfolioStore.isEntitiesLoaded()) {
    return true;
  } else {
    return portfolioStore.resolvePortfolio();
  }
};
