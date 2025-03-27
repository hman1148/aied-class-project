export type Response = {
  message: string;
  success: boolean;
};

export type ItemResponse<T> = Response & {
  item: T;
};

export type ItemsResponse<T> = Response & {
  items: T[];
};
