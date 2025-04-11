package com.ai_tutor.Services;

import com.ai_tutor.Models.Answer;
import com.ai_tutor.Models.Stock;
import com.ai_tutor.Models.TutorQuestion;
import com.ai_tutor.Utilities.PromptTemplates;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GptService {

    private static final String OPEN_AI_URL = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;

    private final RestTemplate restTemplate;

    public GptService() {
        Dotenv dotenv = Dotenv.load();

        this.apiKey = dotenv.get("OPENAI_API_KEY");
        restTemplate = new RestTemplate();
    }

    public TutorQuestion generateTutorQuestion() throws JsonProcessingException {
        return this.generateTutorQuestion(null, null);
    }

    public TutorQuestion generateTutorQuestion(Stock stock) throws JsonProcessingException {
        return this.generateTutorQuestion(stock, null);
    }

    public TutorQuestion generateTutorQuestion(String customScenario) throws JsonProcessingException {
        return this.generateTutorQuestion(null, customScenario);
    }

    public TutorQuestion generateTutorQuestion(Stock stock, String customScenario) throws JsonProcessingException {
        String prompt = "";
        System.out.println("API Key: " + this.apiKey);
        if (stock != null) {
            prompt = String.format(PromptTemplates.STOCK_SPECIFIC_PROMPT,
                    stock.getTickerSymbol(), stock.getTickerSymbol(), stock.getTickerSymbol());
            System.out.println(prompt + "prompt");
        } else if (customScenario != null) {
            prompt = String.format(PromptTemplates.CUSTOM_SCENARIO_PROMPT, customScenario);
        } else {
            prompt = PromptTemplates.DEFAULT_QUESTION_PROMPT;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.apiKey);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", "gpt-4");
        requestBodyMap.put("messages", List.of(
                Map.of("role", "system", "content", "You are an investing tutor with up-to-date knowledge about financial markets and investment strategies."),
                Map.of("role", "user", "content", prompt)
        ));

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = this.restTemplate.postForEntity(OPEN_AI_URL, entity, String.class);
            System.out.println("Response: " + response.getBody());
            ObjectMapper responseObjectMapper = new ObjectMapper();
            JsonNode rootNode = responseObjectMapper.readTree(response.getBody());

            String content = rootNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            System.out.println("Content to deserialize: " + content);
            TutorQuestion tutorQuestion = responseObjectMapper.readValue(content, TutorQuestion.class);
            System.out.println("Deserialized successfully");
            return tutorQuestion;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String generateRandomScenario() {
        String prompt = PromptTemplates.RANDOM_SCENARIO_GENERATOR_PROMPT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.apiKey);

        String requestBody = String.format(PromptTemplates.GPT_REQUEST_TEMPLATE, prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> respones = this.restTemplate.postForEntity(OPEN_AI_URL, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(respones.getBody());

            return rootNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
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