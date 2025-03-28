export type Answer = {
  option: string;
  cost: number;
  explanation: string;
};

export const initialAnswer = (): Answer => ({
  option: '',
  cost: 0,
  explanation: '',
});

export type CorrectAnswer = {
  isCorrect: boolean;
  correctAnswer: Answer;
  explanation: string;
  portfolioChange: number;
  newPortfolioBalance: number;
};

export const initialCorrectAnswer = (): CorrectAnswer => ({
  isCorrect: false,
  correctAnswer: initialAnswer(),
  explanation: '',
  portfolioChange: 0,
  newPortfolioBalance: 0,
});
