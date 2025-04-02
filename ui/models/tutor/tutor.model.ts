import { Answer, initialAnswer } from '../answer/answer.model';

export type TutorQuestion = {
  id: string;
  question: string;
  answers: Answer[];
  correctAnswer: Answer;
  userAnswer: Answer;
  isCorrect: boolean;
};

export const initialTutorQuestion = (): TutorQuestion => ({
  id: '',
  question: '',
  answers: [],
  correctAnswer: initialAnswer(),
  userAnswer: initialAnswer(),
  isCorrect: false,
});
