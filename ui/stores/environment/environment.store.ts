import { signalStore, withMethods, withState } from '@ngrx/signals';
import { withEntities } from '@ngrx/signals/entities';

export type EnvironmentStoreState = {
  apiUrl: string;
  production: boolean;
};

export const initialEnvironmentState = (): EnvironmentStoreState => ({
  apiUrl: 'http://localhost:3000',
  production: false,
});

export const EnvironmentStore = signalStore(
  { providedIn: 'root' },
  withState(initialEnvironmentState()),
  withMethods((store) => ({
    apiUrl: () => {
      return store.apiUrl();
    },
  }))
);
