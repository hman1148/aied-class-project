package com.ai_tutor.ai_tutor.Services.StocksService;

import Models.Answer;
import Models.TutorQuestion;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GptService {

    private static final String OPEN_AI_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private final String apiKey;

    private final RestTemplate restTemplate;

    public GptService(String apiKey) {
        this.apiKey = apiKey;
        restTemplate = new RestTemplate();
    }

    public TutorQuestion generateTutorQuestion() {
        String prompt = """
        Create a JSON object representing a multiple choice investing-related question.
        The format should be:
        {
          "question": "Your question here?",
          "answers": [
            {"option": "Option A", "cost": 100.0, "explanation": "Explanation for Option A"},
            {"option": "Option B", "cost": 50.0, "explanation": "Explanation for Option B"},
            {"option": "Option C", "cost": -20.0, "explanation": "Explanation for Option C"},
            {"option": "Option D", "cost": -50.0, "explanation": "Explanation for Option D"}
          ],
          "correctAnswer": {"option": "Option A", "cost": 100.0, "explanation": "Explanation for Option A"}
        }
        Make the question relevant to stock investing or portfolio decision-making for a beginner.
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String requestBody = """
        {
            "model": "gpt-3.5-turbo",
            "messages": [
                {"role": "system", "content": "You are an investing tutor."},
                {"role": "user", "content": "%s"}
            ],
            "temperature": 0.7
        }
        """.formatted(prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = this.restTemplate.postForEntity(OPEN_AI_URL, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            return objectMapper.readValue(content, TutorQuestion.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public double getProfit(TutorQuestion tutorQuestion) {
        Answer correctAnswer = tutorQuestion.getCorrectAnswer();
        return correctAnswer != null ? correctAnswer.getCost() : 0.0;
    }
}