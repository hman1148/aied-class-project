package com.ai_tutor.Services;

import com.ai_tutor.Models.Stock;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StockService {

    private final ObjectMapper objectMapper;
    private final ArrayList<Stock> stocks;
    private boolean stocksLoaded = false;

    public StockService() {
        this.objectMapper = new ObjectMapper();
        this.stocks = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing StockService...");
        getStocks();  // This will trigger loading if not already loaded
        System.out.println("Loaded " + stocks.size() + " stocks from file");
    }

    public List<Stock> loadStocksFromFile() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("top_50_tickers.json")) {
            if (inputStream == null) {
                throw new RuntimeException("top_50_tickers.json not found");
            }

            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode stocksNode = rootNode.path("stocks");

            List<Stock> stockList = new ArrayList<>();

            for (JsonNode stockNode : stocksNode) {
                String tickerSymbol = stockNode.path("tickerSymbol").asText();
                double open = stockNode.path("open").asDouble();
                double high = stockNode.path("high").asDouble();
                double low = stockNode.path("low").asDouble();
                double price = stockNode.path("price").asDouble();
                double previousClose = stockNode.path("previousClose").asDouble();
                double change = stockNode.path("change").asDouble();
                double changePercent = stockNode.path("changePercent").asDouble();
                long volume = stockNode.path("volume").asLong();
                String lastUpdatedStr = stockNode.path("lastUpdated").asText();

                // Create a Stock object with PRICE already set
                Stock stock = new Stock(
                        tickerSymbol, 0, 0, tickerSymbol,
                        open, high, low, price,
                        previousClose, change, changePercent,
                        volume, lastUpdatedStr
                );
                stockList.add(stock);
            }

            return stockList;

        } catch (Exception ex) {
            throw new RuntimeException("Failed to load stock data from file: " + ex.getMessage(), ex);
        }
    }


    public ArrayList<Stock> getStocks() {
        // Load stocks only once
        if (!stocksLoaded) {
            List<Stock> stockList = loadStocksFromFile();
            stocks.clear(); // Clear any existing data first
            stocks.addAll(stockList);
            stocksLoaded = true;
        }
        return stocks;
    }

    public HashMap<String, Stock> getStocksMap() {
        HashMap<String, Stock> stockMap = new HashMap<>();
        for (Stock stock : getStocks()) {
            stockMap.put(stock.getTickerSymbol(), stock);
        }
        return stockMap;
    }


}