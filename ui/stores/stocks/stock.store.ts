import {
  patchState,
  signalStore,
  type,
  withMethods,
  withState,
} from '@ngrx/signals';
import { setAllEntities, withEntities } from '@ngrx/signals/entities';
import { inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';

import { initialStockStoreState } from './stock-store.state';
import { Stock } from '../../models';
import { StockService } from '../../services/stocks/stocks.services';

const collection = 'stock';

export const StockStore = signalStore(
  { providedIn: 'root' },
  withState(initialStockStoreState()),
  withEntities({ entity: type<Stock>(), collection: collection }),
  withMethods((store, stockService = inject(StockService)) => ({
    resolveStocks: async () => {
      if (store.isEntitiesLoaded()) {
        return true;
      }

      patchState(store, { isLoading: true });

      try {
        const { items, success } = await firstValueFrom(
          stockService.getStocks()
        );

        if (success) {
          patchState(
            store,
            setAllEntities(items, {
              collection: collection,
              selectId: (stock: Stock) => stock.tickerSymbol,
            }),
            {
              isLoading: false,
              isEntitiesLoaded: true,
            }
          );
        }
      } catch (error) {
        console.error(error);
      }

      return true;
    },

    resolveStock: async (tickerSymbol: string) => {
      const stock = store
        .stockEntities()
        .find((stock) => stock.tickerSymbol === tickerSymbol);
      patchState(store, {
        currentStock: stock,
      });
    },
  }))
);
