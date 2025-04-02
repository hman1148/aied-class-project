import { Portfolio } from '../../models';
import {
  initialPortfolioSummary,
  PortfolioSummary,
} from '../../models/portfolio/portfolio-summary.model';

export type PortfolioStoreState = {
  isLoading: boolean;
  isEntitiesLoaded: boolean;
  currentPortfolio: PortfolioSummary;
  portfolioTotalValue: number;
  portfolioTotalInvested: number;
  portfolioTotalGrowth: number;
};

export const initialPortfolioStoreState = (): PortfolioStoreState => ({
  isLoading: false,
  isEntitiesLoaded: false,
  currentPortfolio: initialPortfolioSummary(),
  portfolioTotalValue: 0,
  portfolioTotalInvested: 0,
  portfolioTotalGrowth: 0,
});
