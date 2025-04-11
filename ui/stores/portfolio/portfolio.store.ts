import { inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { updateAllEntities, withEntities } from '@ngrx/signals/entities';
import {
  patchState,
  signalStore,
  type,
  withMethods,
  withState,
} from '@ngrx/signals';

import { initialPortfolioStoreState } from './portfolio.state';
import { CorrectAnswer, Portfolio } from '../../models';
import { PortfolioService } from '../../services/portfolio/portfolio.service';

const collection = 'portfolio';

export const PortfolioStore = signalStore(
  { providedIn: 'root' },
  withState(initialPortfolioStoreState()),
  withEntities({ entity: type<Portfolio>(), collection: collection }),
  withMethods((store, portfolioService = inject(PortfolioService)) => ({
    resolvePortfolio: async () => {
      if (store.isEntitiesLoaded()) {
        return true;
      }

      patchState(store, { isLoading: true });
      try {
        const { item, success } = await firstValueFrom(
          portfolioService.getPortfolioSummary()
        );

        if (success) {
          patchState(store, {
            currentPortfolio: item,
            isEntitiesLoaded: true,
          });
        }
      } catch (error) {
        console.error(error);
      }
      patchState(store, { isLoading: false });
      return true;
    },

    calculatePortfolioGrowth: () => {
      if (!store.currentPortfolio() || !store.currentPortfolio().stocks) {
        return 0;
      }

      const totalCurrent =
        store.currentPortfolio().totalCurrentValue -
        store.currentPortfolio().totalInvested;
      const totalInvested = store.currentPortfolio().totalInvested;

      if (totalInvested === 0) {
        return 0;
      }
      const totalGrowth = (totalCurrent / totalInvested) * 100;

      return totalGrowth;
    },

    calculatePortfolioTotalValue: () => {
      if (!store.currentPortfolio()) return 0;

      let totalValue = store.currentPortfolio().cash || 0;

      // Add value of all stocks
      if (store.currentPortfolio().stocks) {
        totalValue += store
          .currentPortfolio()
          .stocks.reduce(
            (sum, stock) => sum + stock.price * (stock.volume || 0),
            0
          );
      }

      return totalValue;
    },

    updatePortfolio: (correctAnswer: CorrectAnswer) => {
      patchState(store, {
        currentPortfolio: {
          ...store.currentPortfolio(),
          cash: correctAnswer.newPortfolioBalance,
        }
      })
    }
  }))
);
