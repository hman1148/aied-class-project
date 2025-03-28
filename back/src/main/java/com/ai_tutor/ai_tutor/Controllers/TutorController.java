package com.ai_tutor.ai_tutor.Controllers;


import Models.Requests.StudentAnswerRequest;
import Models.Response.ItemResponse;
import Models.TutorQuestion;
import com.ai_tutor.ai_tutor.Services.StocksService.GptService;
import com.ai_tutor.ai_tutor.Services.StocksService.PortfolioService;
import com.ai_tutor.ai_tutor.Services.StocksService.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/tutor")
@CrossOrigin(origins = "*") // Allow CORS for testing from angular
public class TutorController {

    @Autowired
    private final TutorService tutorService;

    @Autowired
    private final GptService gptService;

    @Autowired
    private final PortfolioService portfolioService;

    private TutorQuestion currentQuestion;
    private ArrayList<TutorQuestion> questionHistory = new ArrayList<>();

    public TutorController(TutorService tutorService, GptService gptService, PortfolioService portfolioService) {
        this.tutorService = tutorService;
        this.gptService = gptService;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/question")
    public ResponseEntity<ItemResponse<TutorQuestion>> getQuestion() {
        TutorQuestion tutorQuestion = this.tutorService.generateTutorQuestion();
        this.currentQuestion = tutorQuestion;

        ItemResponse<TutorQuestion> response = new ItemResponse<>(tutorQuestion, "Question generated successfully", true);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ItemResponse<List<TutorQuestion>>> getQuestionHistory() {
        ItemResponse<List<TutorQuestion>> response = new ItemResponse<>(questionHistory,
                "Question history retrieved successfully", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-history")
    public ResponseEntity<ItemResponse<String>> resetQuestionHistory() {
        this.questionHistory.clear();
        currentQuestion = null;

        ItemResponse<String> response = new ItemResponse<>("Question history reset successfully",
                "Question history reset successfully", true);
        return ResponseEntity.ok(response);
    }
}
