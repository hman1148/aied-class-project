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
      tutorService = inject(TutorService),
      messageService = inject(MessageService)
    ) => ({
      loadHistory: async () => {
        patchState(store, { isLoading: true });

        tutorService.getHistory().subscribe({
          next: ({ items, success }) => {
            if (success) {
              patchState(
                store,
                setAllEntities(items, {
                  collection: collection,
                  selectId: (tutor: TutorQuestion) => tutor.id,
                }),
                { isEntitiesLoaded: true }
              );
              messageService.add({
                severity: 'success',
                summary: 'Success',
                detail: 'History loaded successfully',
              });
            }
          },
          error: ({ message }) => {
            console.error(message);
          },
          complete: () => patchState(store, { isLoading: false }),
        });
      },

      getQuestion: async () => {
        patchState(store, { isLoading: true });

        try {
          const { item, success } = await firstValueFrom(
            tutorService.getQuestion()
          );

          if (success) {
            patchState(
              store,
              addEntity(item, {
                collection: collection,
                selectId: (tutor: TutorQuestion) => tutor.id,
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

      getStockQuestion: (ticker: string) => {
        tutorService.getStockQuestion(ticker).subscribe({
          next: ({ item, success }) => {
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
            }
          },
          error: ({ message }) => {
            console.error(message);
            messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Error getting question',
            });
          },
          complete: () => patchState(store, { isLoading: false }),
        });
      },

      getRandomQuestion: () => {
        patchState(store, { isLoading: true });

        tutorService.getRandomQuestion().subscribe({
          next: ({ item, success }) => {
            if (success) {
              patchState(
                store,
                addEntity(item, {
                  collection: collection,
                  selectId: (tutor: TutorQuestion) => tutor.id,
                }),
                {
                  currentTutorQuestion: item,
                  isLoading: false,
                }
              );
            }
          },
          error: ({ message }) => {
            console.error(message);
            messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Error getting question',
            });
          },
          complete: () => patchState(store, { isLoading: false }),
        });
      },

      submitAnswer: async () => {
        tutorService
          .getAnswer(store.currentTutorQuestion().question)
          .subscribe({
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

        tutorService.resetHistory().subscribe({
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
