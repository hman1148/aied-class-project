package com.ai_tutor.ai_tutor.Services.StocksService;

import Models.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockService {

    private final String API_KEY;
    private static final String BASE_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public StockService() {
        Dotenv dotenv = Dotenv.load();
        this.API_KEY = dotenv.get("ALPHAVANTAGE_API_KEY");
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public boolean updateStockFromAPI(Stock stock) {
        String url = String.format(BASE_URL, stock.getTickerSymbol(), API_KEY);

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode quoteNode = rootNode.path("Global Quote");

            if (quoteNode.isMissingNode() || quoteNode.isEmpty()) {
                System.out.println("No quote data found for: " + stock.getTickerSymbol());
                return false;
            }

            double open = this.getDouble(quoteNode, "02. open");
            double high = this.getDouble(quoteNode, "03. high");
            double low = this.getDouble(quoteNode, "04. low");
            double price = this.getDouble(quoteNode, "05. price");
            double previousClose = this.getDouble(quoteNode, "08. previous close");
            double change = this.getDouble(quoteNode, "09. change");
            double changePercent = this.parsePercentage(quoteNode.get("10. change percent").asText());
            long volume = this.getLong(quoteNode, "06. volume");


            stock.updateQuoteData(open, high, low, price, previousClose, change, changePercent, volume);
            return true;

        } catch (Exception ex) {
            System.err.println("Error getting response for " + stock.getTickerSymbol());
            ex.printStackTrace();
            return false;
        }

    }

    public List<String> loadTickersFromFile() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("top_50_tickers.json")) {
            if (inputStream == null) {
                throw new RuntimeException("top_50_tickers not found");
            }
            return this.objectMapper.readValue(inputStream, new TypeReference<List<String>>() {});
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load ticker list.", ex);
        }
    }

    private double getDouble(JsonNode node, String field) {
        return node.has(field) ? Double.parseDouble(node.get(field).asText()) : 0;
    }

    private long getLong(JsonNode node, String field) {
        return node.has(field) ? Long.parseLong(node.get(field).asText()) : 0;
    }

    private double parsePercentage(String percentText) {
        if (percentText == null || percentText.isEmpty()) return 0.0;
        return Double.parseDouble(percentText.replace("%", ""));
    }
}
