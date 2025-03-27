package com.ai_tutor.ai_tutor.Services.StocksService;

import Models.TutorQuestion;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TutorService {

    private final GptService gptService;
    private final PortfolioService portfolioService;

    private List<String> answerHistory;

    public TutorService(GptService gptService, PortfolioService portfolioService) {
        this.gptService = gptService;
        this.portfolioService = portfolioService;
        this.answerHistory = new ArrayList<>();
    }
    /**
     * Generates a tutor question using the GptService.
     *
     * @return a TutorQuestion object containing the generated question, answers, and correct answer.
     */
    public TutorQuestion generateTutorQuestion() {
        return this.gptService.generateTutorQuestion();
    }

    public void handleAnswer(TutorQuestion tutorQuestion) {
        if (tutorQuestion.isCorrect()) {
            double profit = this.gptService.getProfit(tutorQuestion);
            this.portfolioService.setProfit(profit);
        } else {
            double loss = this.gptService.getProfit(tutorQuestion);
            this.portfolioService.setProfit(-loss);
        }

        this.answerHistory.add(tutorQuestion.getCorrectAnswer().getExplanation());
    }
}

