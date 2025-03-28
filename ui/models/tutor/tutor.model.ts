import { Answer, initialAnswer } from '../answer/answer.model';

export type TutorQuestion = {
  question: string;
  answers: Answer[];
  correctAnswer: Answer;
  userAnswer: Answer;
  isCorrect: boolean;
};

export const initialTutorQuestion = (): TutorQuestion => ({
  question: '',
  answers: [],
  correctAnswer: initialAnswer(),
  userAnswer: initialAnswer(),
  isCorrect: false,
});
