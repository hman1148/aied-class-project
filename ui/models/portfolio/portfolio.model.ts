import { Stock } from '../stock/stock.model';

export type Portfolio = {
  cash: number;
  stocks: Stock[];
};
