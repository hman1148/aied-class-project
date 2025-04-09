import { Routes } from '@angular/router';
import { historyResolver } from './sub-components/history-component/history-component/history.resolver';
import { portfolioResolver, stockResolver } from '../app.resolver';
import { AppComponent } from './app.component';
import { PortfolioStore } from '../../stores';
import { inject } from '@angular/core';

export const routes: Routes = [
  {
    path: '',
    component: AppComponent,
    resolve: {
      tutorHistory: historyResolver,
      stocks: stockResolver,
      portfolio: portfolioResolver,
    },
  },
];
