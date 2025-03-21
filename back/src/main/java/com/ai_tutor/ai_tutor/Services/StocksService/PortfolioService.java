package com.ai_tutor.ai_tutor.Services.StocksService;

import Models.Stock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PortfolioService {

    private final ArrayList<Stock> portfolio = new ArrayList<>();
    private double cash = 100_000.0;

    public boolean addStock(Stock stock) {
        double cost = stock.getPurchasedValue() * stock.getQuantity();

        if (cost > cash) {
            return false;
        }

        cash -= cost;
        portfolio.add(stock);
        return true;
    }

    public ArrayList<Stock> getPortfolio() {
        return this.portfolio;
    }

    public double getCashRemaining() {
        return this.cash;
    }

    public double getTotalInvested() {
        return this.portfolio.stream()
                .mapToDouble(stock -> stock.getPurchasedValue() * stock.getQuantity())
                .sum();
    }

    public double getTotalCurrentValue() {
        return this.portfolio.stream()
                .mapToDouble(stock -> stock.getCurrentValue() * stock.getQuantity())
                .sum();
    }

    public double getTotalProfit() {
        return this.portfolio.stream()
                .mapToDouble(Stock::getProfit)
                .sum();
    }

    public void resetPortfolio() {
        this.portfolio.clear();
        this.cash = 100_000.0;
    }

}
