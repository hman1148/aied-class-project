package com.ai_tutor.Services;

import com.ai_tutor.Models.Stock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StockService {

    private final String API_KEY;
    private static final String BASE_URL = "https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=%s&apikey=%s";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private ArrayList<Stock> stocks;

    public StockService() {
        Dotenv dotenv = Dotenv.load();
        this.API_KEY = dotenv.get("ALPHAVANTAGE_API_KEY");
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.stocks = new ArrayList<>();
    }

    public boolean updateStocksFromAPI(List<Stock> stockList) {
        // Prepare the list of tickers for the bulk request
        String tickers = String.join(",", stockList.stream().map(Stock::getTickerSymbol).toArray(String[]::new));
        String url = String.format(BASE_URL, tickers, API_KEY);

        try {
            // Send the bulk request to Alpha Vantage
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode quoteArray = rootNode.path("stockQuotes");

            if (quoteArray.isArray()) {
                for (JsonNode quoteNode : quoteArray) {
                    String ticker = quoteNode.path("1. symbol").asText();
                    Stock stock = stockList.stream().filter(s -> s.getTickerSymbol().equals(ticker)).findFirst().orElse(null);

                    if (stock != null) {
                        double open = this.getDouble(quoteNode, "2. price");
                        double high = this.getDouble(quoteNode, "3. high");
                        double low = this.getDouble(quoteNode, "4. low");
                        double price = this.getDouble(quoteNode, "5. price");
                        double previousClose = this.getDouble(quoteNode, "8. previous close");
                        double change = this.getDouble(quoteNode, "9. change");
                        double changePercent = this.parsePercentage(quoteNode.get("10. change percent").asText());
                        long volume = this.getLong(quoteNode, "6. volume");

                        stock.updateQuoteData(open, high, low, price, previousClose, change, changePercent, volume);
                    }
                }
                return true;
            }

            return false;

        } catch (Exception ex) {
            System.err.println("Error getting response for stock batch.");
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

    public ArrayList<Stock> getStocks() {
        List<String> tickers = this.loadTickersFromFile();

        // Create a list of stock objects
        List<Stock> stockList = new ArrayList<>();
        for (String ticker : tickers) {
            stockList.add(new Stock(ticker, 0, 0, ticker));
        }

        // Fetch stock data in bulk
        boolean success = this.updateStocksFromAPI(stockList);

        if (success) {
            this.stocks.addAll(stockList);
        } else {
            System.out.println("Failed to update stock data.");
        }

        return this.stocks;
    }

    public HashMap<String, Stock> getStocksMap() {
        HashMap<String, Stock> stockMap = new HashMap<>();
        for (Stock stock : this.stocks) {
            stockMap.put(stock.getTickerSymbol(), stock);
        }
        return stockMap;
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
