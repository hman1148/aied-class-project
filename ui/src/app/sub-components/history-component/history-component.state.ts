export type HistoryComponentState = {
  visible: boolean;
  loading: boolean;
};

export const initialHistoryState = (): HistoryComponentState => ({
  visible: false,
  loading: false,
});
