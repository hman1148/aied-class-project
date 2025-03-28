import {
  CorrectAnswer,
  initialCorrectAnswer,
  initialTutorQuestion,
  TutorQuestion,
} from '../../models';

export type TutorStoreState = {
  isLoading: boolean;
  isEntitiesLoaded: boolean;
  currentTutorQuestion: TutorQuestion;
  currentCorrecAnswer: CorrectAnswer;
};

export const initialTutorStoreState = (): TutorStoreState => ({
  isLoading: false,
  isEntitiesLoaded: false,
  currentCorrecAnswer: initialCorrectAnswer(),
  currentTutorQuestion: initialTutorQuestion(),
});
