import { Routes } from '@angular/router';
import { historyResolver } from './sub-components/history-component/history-component/history.resolver';
import { stockResolver } from '../app.resolver';
import { AppComponent } from './app.component';

export const routes: Routes = [
  {
    path: '',
    component: AppComponent,
    resolve: {
      tutorHistory: historyResolver,
      stocks: stockResolver,
    },
  },
];
