import { inject } from '@angular/core';
import { HistoryStore } from '../../../../../stores';
import { ResolveFn } from '@angular/router';

export const historyResolver: ResolveFn<Promise<boolean>> = async (...args) => {
  const historyStore = inject(HistoryStore);

  if (!historyStore.isLoading() && historyStore.isEntitiesLoaded()) {
    return true;
  } else {
    return historyStore.resolveHistory();
  }
};
