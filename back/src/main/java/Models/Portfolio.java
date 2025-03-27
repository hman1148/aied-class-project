package Models;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {

    private List<Stock> stocks;
    private double cash;

    public Portfolio() {
        this.stocks = new ArrayList<>();
        this.cash = 100_000.0;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public void addStock (Stock stock) {
        this.stocks.add(stock);
    }

    public void removeStock(Stock stock) {
        this.stocks.remove(stock);
    }


    public double getTotalInvested() {
        return this.stocks.stream()
                .mapToDouble(stock -> stock.getPurchasedValue() * stock.getQuantity())
                .sum();
    }

    public double getTotalCurrentValue() {
        return this.stocks.stream()
                .mapToDouble(stock -> stock.getCurrentValue() * stock.getQuantity())
                .sum();
    }

    public double getTotalProfit() {
        return this.stocks.stream()
                .mapToDouble(Stock::getProfit)
                .sum();
    }
}

