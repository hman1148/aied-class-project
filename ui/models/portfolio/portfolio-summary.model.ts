import { Stock } from '../stock/stock.model';

export type PortfolioSummary = {
  cash: number;
  totalInvested: number;
  totalCurrentValue: number;
  totalProfit: number;
  stocks: Stock[];
};

export const initialPortfolioSummary = (): PortfolioSummary => ({
  cash: 0,
  totalInvested: 0,
  totalCurrentValue: 0,
  totalProfit: 0,
  stocks: [],
});
