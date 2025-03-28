import { Portfolio } from '../portfolio/portfolio.model';
import { Stock } from './stock.model';

export type StockPurchaseModel = {
  purchased: Stock;
  portfolio: Portfolio;
  cashRemaining: number;
  totalInvested: number;
  totalProfit: number;
};
