package com.ai_tutor.Controllers;


import com.ai_tutor.Models.Requests.StudentAnswerRequest;
import com.ai_tutor.Models.Response.ItemResponse;
import com.ai_tutor.Models.Response.ItemsResponse;
import com.ai_tutor.Models.Stock;
import com.ai_tutor.Models.TutorQuestion;
import com.ai_tutor.Services.GptService;
import com.ai_tutor.Services.PortfolioService;
import com.ai_tutor.Services.StockService;
import com.ai_tutor.Services.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private final TutorService tutorService;

    @Autowired
    private final GptService gptService;

    @Autowired
    private final StockService stockService;

    @Autowired
    private final PortfolioService portfolioService;

    private TutorQuestion currentQuestion;
    private final ArrayList<TutorQuestion> questionHistory = new ArrayList<>();

    public TutorController(TutorService tutorService, GptService gptService,
                           PortfolioService portfolioService, StockService stockService) {
        this.tutorService = tutorService;
        this.gptService = gptService;
        this.portfolioService = portfolioService;
        this.stockService = stockService;
    }

    @GetMapping("/question")
    public ResponseEntity<ItemResponse<TutorQuestion>> getQuestion() {
        TutorQuestion tutorQuestion = this.gptService.generateTutorQuestion();
        this.currentQuestion = tutorQuestion;

        ItemResponse<TutorQuestion> response = new ItemResponse<>(tutorQuestion, "Question generated successfully", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/question/{ticker}")
    public ResponseEntity<?> getQuestion(@RequestParam String ticker) {
        Stock foundStock = this.stockService.getStocksMap().get(ticker);

        try {
            TutorQuestion tutorQuestion = this.gptService.generateTutorQuestion(foundStock);

            if (tutorQuestion == null) {
                ItemResponse<String> failedResponse = new ItemResponse<>("Failed response", "Failed to generate question", false);
                return ResponseEntity.badRequest().body(failedResponse);
            }
            ItemResponse<TutorQuestion> successData = new ItemResponse<>(tutorQuestion, "Generated Question", true);
            return ResponseEntity.ok(successData);
        } catch (Exception ex) {
            ItemResponse<String> failedResponse = new ItemResponse<>("Failed response", "Hit a system error", false);
            return ResponseEntity.badRequest().body(failedResponse);
        }
    }

    @GetMapping("/question/random-scenario")
    public ResponseEntity<?> getScenarioQuestion() {
        try {
            String scenario = this.gptService.generateRandomScenario();
            TutorQuestion tutorQuestion = this.gptService.generateTutorQuestion(scenario);

            if (tutorQuestion == null) {
                ItemResponse<String> failedResponse = new ItemResponse<>("Failed response", "Failed to generate question", false);
                return ResponseEntity.badRequest().body(failedResponse);
            }

            ItemResponse<TutorQuestion> successData = new ItemResponse<>(tutorQuestion, "Generated Question", true);
            return ResponseEntity.ok(successData);
        } catch (Exception ex) {
            ItemResponse<String> failedResponse = new ItemResponse<>("Failed response", "Hit a system error", false);
            return ResponseEntity.badRequest().body(failedResponse);
        }
    }

    @PostMapping("/answer")
    public ResponseEntity<ItemResponse<HashMap<String, Object>>> getStudentResponse
            (@RequestBody StudentAnswerRequest studentAnswerRequest) {

        if (currentQuestion == null) {
            return ResponseEntity.badRequest().body(new ItemResponse<>(null, "No question generated", false));
        }

        String selectedQuestion = studentAnswerRequest.getSelectedQuestion();
        currentQuestion.setUserAnswer(selectedQuestion);

        // Check if answer is correct
        boolean isCorrect = currentQuestion.getCorrectAnswer().getOption().equals(selectedQuestion);
        currentQuestion.setCorrect(isCorrect);

        // Add question to history
        questionHistory.add(currentQuestion);

        double profit = this.gptService.getProfit(currentQuestion);
        if (isCorrect) {
            this.portfolioService.setProfit(profit);
        } else {
            this.portfolioService.setProfit(-profit);
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("isCorrect", isCorrect);
        responseData.put("correctAnswer", currentQuestion.getCorrectAnswer());
        responseData.put("explanation", currentQuestion.getCorrectAnswer().getExplanation());
        responseData.put("portfolioChange", profit);
        responseData.put("newPortfolioBalance", this.portfolioService.getCashRemaining());

        ItemResponse<HashMap<String, Object>> response = new ItemResponse<>(responseData,
                "Answer processed successfully", true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<ItemsResponse<TutorQuestion>> getQuestionHistory() {
        ItemsResponse<TutorQuestion> response = new ItemsResponse<>(questionHistory,
                "Question history retrieved successfully", true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reset-history")
    public ResponseEntity<ItemResponse<String>> resetQuestionHistory() {
        this.questionHistory.clear();
        currentQuestion = null;

        ItemResponse<String> response = new ItemResponse<>("Question history reset successfully",
                "Question history reset successfully", true);
        return ResponseEntity.ok(response);
    }
}
