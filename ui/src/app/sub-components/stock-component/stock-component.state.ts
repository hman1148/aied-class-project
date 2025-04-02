import { inintialStock, Stock, TutorQuestion } from '../../../../models';

export type StockComponentState = {
  quantity: number;
  showPurchasingDialog: boolean;
  selectedAnswer: string;
  selectedStock: Stock;
};

export const initialStockComponentState = (): StockComponentState => ({
  quantity: 0,
  showPurchasingDialog: false,
  selectedAnswer: '',
  selectedStock: inintialStock(),
});
