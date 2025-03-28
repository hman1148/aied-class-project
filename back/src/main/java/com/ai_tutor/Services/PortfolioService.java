package com.ai_tutor.Services;

import com.ai_tutor.Models.Portfolio;
import com.ai_tutor.Models.Stock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PortfolioService {

    private final Portfolio portfolio;

    public PortfolioService() {
        this.portfolio = new Portfolio();
    }

    public boolean addStock(Stock stock) {
        double cost = stock.getPurchasedValue() * stock.getQuantity();

        if (cost > this.portfolio.getCash()) {
            return false;
        }

        this.portfolio.setCash(portfolio.getCash() - cost);
        this.portfolio.addStock(stock);
        return true;
    }

    public void setProfit(double amount) {
        this.portfolio.setCash(portfolio.getCash() + amount);
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public double getCashRemaining() {
        return this.portfolio.getCash();
    }

    public double getTotalInvested() {
        return this.portfolio.getTotalInvested();
    }

    public double getTotalCurrentValue() {
        return this.portfolio.getTotalCurrentValue();
    }

    public double getTotalProfit() {
        return this.portfolio.getTotalProfit();
    }

    public void resetPortfolio() {
        this.portfolio.setStocks(new ArrayList<>());
        this.portfolio.setCash(100_000.0);
    }
}
