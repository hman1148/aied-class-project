package com.ai_tutor.ai_tutor.Controllers;


import Models.Requests.StockPurchaseRequest;
import Models.Response.ItemResponse;
import Models.Response.ItemsResponse;
import Models.Stock;
import com.ai_tutor.ai_tutor.Services.StocksService.PortfolioService;
import com.ai_tutor.ai_tutor.Services.StocksService.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@CrossOrigin(origins = "*") // Allow CORS for testing from angular
public class StockController {

    @Autowired
    private final StockService stockService;

    @Autowired
    private final PortfolioService portfolioService;

    public StockController(StockService stockService, PortfolioService portfolioService) {
        this.stockService = stockService;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/all")
    public ResponseEntity<ItemsResponse<Stock>> getAllStocks() {
        List<String> tickers = stockService.loadTickersFromFile();

        List<Stock> stockList = new ArrayList<>();
        for (String ticker : tickers) {
            Stock stock = new Stock(ticker, 0, 0, ticker);
            boolean success = stockService.updateStockFromAPI(stock);

            if (success) {
                stockList.add(stock);
            } else {
                System.out.println("Failed to update stock data for: " + ticker);
            }
        }

        try {
            Thread.sleep(150000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ItemsResponse<Stock> response = new ItemsResponse<>(stockList, "Stock data retrieved", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@RequestBody StockPurchaseRequest request) {
        String ticker = request.getTickerSymbol();
        int quantity = request.getQuantity();

        Stock stock = new Stock(ticker, quantity, 0, ticker);
        boolean success = stockService.updateStockFromAPI(stock);

        if (!success) {
            return ResponseEntity
                    .badRequest()
                    .body(new ItemResponse<String>("", "Failed to retrieve stock data", false));
        }

        stock.setPurchasedValue(stock.getPrice());
        stock.updateProfit();

        boolean added = this.portfolioService.addStock(stock);

        if (!added) {
            return ResponseEntity
                    .badRequest()
                    .body(new ItemResponse<String>("", "Insufficient funds", false));
        }

        HashMap<String, Object> mapData = new HashMap<>();
        mapData.put("purchased", stock);
        mapData.put("portfolio", this.portfolioService.getPortfolio());
        mapData.put("cashRemaining", portfolioService.getCashRemaining());
        mapData.put("totalInvested", portfolioService.getTotalInvested());
        mapData.put("totalProfit", portfolioService.getTotalProfit());

        ItemResponse<HashMap<String, Object>> response = new ItemResponse<>(mapData, "Stock purchased", true);
        return ResponseEntity.ok(response);
    }

}
