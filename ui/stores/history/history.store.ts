import { TutorQuestion } from './../../models/tutor/tutor.model';
import {
  patchState,
  signalStore,
  type,
  withMethods,
  withState,
} from '@ngrx/signals';
import {
  addEntity,
  setAllEntities,
  withEntities,
} from '@ngrx/signals/entities';
import { inject } from '@angular/core';
import { TutorService } from '../../services/tutor/tutor.service';
import { initialHistoryStoreState } from './history-store.state';
import { firstValueFrom } from 'rxjs';
import { MessageService } from 'primeng/api';

const collection = 'history';

export const HistoryStore = signalStore(
  { providedIn: 'root' },
  withState(initialHistoryStoreState()),
  withEntities({ entity: type<TutorQuestion>(), collection: collection }),
  withMethods(
    (
      store,
      tutorService = inject(TutorService),
      messageService = inject(MessageService)
    ) => ({
      resolveHistory: async () => {
        if (store.isEntitiesLoaded()) {
          return true;
        }

        patchState(store, { isLoading: true });

        try {
          const { items, success } = await firstValueFrom(
            tutorService.getHistory()
          );

          if (success) {
            patchState(
              store,
              setAllEntities(items, {
                collection: collection,
                selectId: (tutor: TutorQuestion) => tutor.question,
              }),
              {
                isEntitiesLoaded: true,
                isLoading: false,
              }
            );
          }
        } catch (error) {
          console.error(error);
        }
        return true;
      },

      getHistoryByQuestion: (question: string) => {
        const tutorQuestion = store.historyEntities().find((tutorQuestion) => {
          return tutorQuestion.question === question;
        });

        patchState(store, { selectedTutorQuestion: tutorQuestion });
      },

      updateHistory: (tutorQuestion: TutorQuestion) => {
        patchState(
          store,
          addEntity(tutorQuestion, {
            collection: collection,
            selectId: (tutor: TutorQuestion) => tutor.question,
          })
        );
      },
    })
  )
);
