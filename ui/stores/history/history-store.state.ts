import { initialTutorQuestion, TutorQuestion } from '../../models';

export type HistoryStoreState = {
  isLoading: boolean;
  isEntitiesLoaded: boolean;
  selectedTutorQuestion: TutorQuestion;
};

export const initialHistoryStoreState = (): HistoryStoreState => ({
  isLoading: false,
  isEntitiesLoaded: false,
  selectedTutorQuestion: initialTutorQuestion(),
});
