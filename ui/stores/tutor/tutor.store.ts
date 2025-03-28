import {
  patchState,
  signalStore,
  type,
  withMethods,
  withState,
} from '@ngrx/signals';
import { initialTutorStoreState } from './tutor-store.model';
import {
  addEntity,
  removeAllEntities,
  setAllEntities,
  withEntities,
} from '@ngrx/signals/entities';
import { TutorQuestion } from '../../models';
import { inject } from '@angular/core';
import { TutorService } from '../../services/tutor/tutor.service';
import { StockService } from '../../services/stocks/stocks.services';
import { firstValueFrom } from 'rxjs';
import { MessageService } from 'primeng/api';

const collection = 'tutor';

export const TutorStore = signalStore(
  { providedIn: 'root' },
  withState(initialTutorStoreState()),
  withEntities({ entity: type<TutorQuestion>(), collection: collection }),
  withMethods(
    (
      store,
      tutorServvice = inject(TutorService),
      stockService = inject(StockService),
      messageService = inject(MessageService)
    ) => ({
      loadHistory: async () => {
        if (store.isEntitiesLoaded()) {
          return true;
        }

        patchState(store, { isLoading: true });

        try {
          const { items, success } = await firstValueFrom(
            tutorServvice.getHistory()
          );

          if (success) {
            patchState(
              store,
              setAllEntities(items, {
                collection: collection,
                selectId: (tutor: TutorQuestion) => tutor.question,
              }),
              {
                isLoading: false,
                isEntitiesLoaded: true,
              }
            );
          }
        } catch (error) {
          console.error(error);
        }
        return true;
      },

      getNewQuestion: async () => {
        patchState(store, { isLoading: true });

        try {
          const { item, success } = await firstValueFrom(
            tutorServvice.getQuestion()
          );

          if (success) {
            patchState(
              store,
              addEntity(item, {
                collection: collection,
                selectId: (tutor: TutorQuestion) => tutor.question,
              }),
              {
                currentTutorQuestion: item,
                isLoading: false,
              }
            );
          } else {
            messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Error getting question',
            });
          }
        } catch (error) {
          console.error(error);
          messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error getting question',
          });
        }
      },

      submitAnswer: async (selectedQuestion: string) => {
        patchState(store, { isLoading: true });

        tutorServvice.getAnswer(selectedQuestion).subscribe({
          next: ({ item, success }) => {
            if (success) {
              patchState(store, {
                currentCorrecAnswer: item,
                isLoading: false,
              });
            }
          },
          error: ({ message }) => {
            console.error(message);
            messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Error getting answer',
            });
          },
          complete: () => patchState(store, { isLoading: false }),
        });
      },

      resetHistory: async () => {
        patchState(store, { isLoading: true });

        tutorServvice.resetHistory().subscribe({
          next: ({ item, success }) => {
            if (success) {
              patchState(store, removeAllEntities({ collection: collection }));
              messageService.add({
                severity: 'success',
                summary: 'Success',
                detail: item,
              });
            }
          },
          error: ({ message }) => {
            console.error(message);
            messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Error resetting history',
            });
          },
          complete: () => patchState(store, { isLoading: false }),
        });
      },
    })
  )
);
