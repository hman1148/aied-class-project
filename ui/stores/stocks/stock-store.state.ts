import {
  inintialStock,
  initialStockPurchaseRequest,
  Stock,
  StockPurchaseRequest,
} from '../../models';

export type StockStoreState = {
  isEntitiesLoaded: boolean;
  isLoading: boolean;
  currentStock: Stock;
  currentStockPurchaseRequest: StockPurchaseRequest;
};

export const initialStockStoreState = (): StockStoreState => ({
  isEntitiesLoaded: false,
  isLoading: false,
  currentStock: inintialStock(),
  currentStockPurchaseRequest: initialStockPurchaseRequest(),
});
