package com.ai_tutor.Controllers;

import com.ai_tutor.Models.Portfolio;
import com.ai_tutor.Models.Response.ItemResponse;
import com.ai_tutor.Services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    @Autowired
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    // Get controller for portfolio
    @GetMapping("/summary")
    public ResponseEntity<ItemResponse<HashMap<String, Object>>> getPortfolioSummary() {
        Portfolio portfolio = this.portfolioService.getPortfolio();

        HashMap<String, Object> summary = new HashMap<>();
        summary.put("cash", portfolio.getCash());
        summary.put("totalInvested", portfolio.getTotalInvested());
        summary.put("totalCurrentValue", portfolio.getTotalCurrentValue());
        summary.put("totalProfit", portfolio.getTotalProfit());
        summary.put("stocks", portfolio.getStocks());

        ItemResponse<HashMap<String, Object>> response = new ItemResponse<>(summary,
                "Portfolio summary retrieved", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<ItemResponse<String>> resetPortfolio() {
        this.portfolioService.resetPortfolio();
        ItemResponse<String> response = new ItemResponse<>("Portfolio reset successfully",
                "Portfolio reset successfully", true);
        return ResponseEntity.ok(response);
    }
}
