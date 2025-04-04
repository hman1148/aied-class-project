import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { StockStore } from '../stores/stocks/stock.store';

export const stockResolver: ResolveFn<Promise<boolean>> = async (...args) => {
  const stockStore = inject(StockStore);

  if (!stockStore.isLoading() && stockStore.isEntitiesLoaded()) {
    return true;
  } else {
    return stockStore.resolveStocks();
  }
};
