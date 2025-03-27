export type Stock = {
  tickerSymobol: string;
  open: number;
  high: number;
  low: number;
  price: number;
  previousClose: number;
  change: number;
  changePercent: number;
  volume: number;
  lastUpdated: Date | null;
};

export const inintialStock = (): Stock => ({
  tickerSymobol: '',
  open: 0,
  high: 0,
  low: 0,
  price: 0,
  previousClose: 0,
  change: 0,
  changePercent: 0,
  volume: 0,
  lastUpdated: null,
});
