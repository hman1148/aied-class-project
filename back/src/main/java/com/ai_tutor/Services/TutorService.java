package com.ai_tutor.Services;

import com.ai_tutor.Models.TutorQuestion;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TutorService {

    private final GptService gptService;
    private final PortfolioService portfolioService;

    private List<String> answerHistory;

    private TutorQuestion currentQuestion;

    public TutorService(GptService gptService, PortfolioService portfolioService) {
        this.gptService = gptService;
        this.portfolioService = portfolioService;
        this.answerHistory = new ArrayList<>();
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

    public TutorQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(TutorQuestion currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
}

