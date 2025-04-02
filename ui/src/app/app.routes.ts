import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { historyResolver } from './sub-components/history-component/history-component/history.resolver';
import { stockResolver } from '../app.resolver';

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
