package com.ai_tutor.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Stock extends InvestmentAbstract {

    private final String tickerSymbol;
    private double open;
    private double high;
    private double low;
    private double price;
    private double previousClose;
    private double change;
    private double changePercent;
    private long volume;
    private LocalDateTime lastUpdated;
    private boolean hasQuoteData = false; // Add this field

    // Create a more complete constructor
    public Stock(String name, double purchasedValue, int quantity, String tickerSymbol,
                 double open, double high, double low, double price,
                 double previousClose, double change, double changePercent,
                 long volume, String lastUpdatedStr) {

        super(name, purchasedValue, quantity);
        this.tickerSymbol = tickerSymbol;
        this.profit = purchasedValue;

        // Initialize all quote data in constructor
        this.open = open;
        this.high = high;
        this.low = low;
        this.price = price;
        this.previousClose = previousClose;
        this.change = change;
        this.changePercent = changePercent;
        this.volume = volume;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        this.lastUpdated = LocalDateTime.parse(lastUpdatedStr, formatter);
        this.hasQuoteData = true;

        this.updateCurrentValue(price);
    }

    public boolean hasQuoteData() {
        return this.hasQuoteData;
    }
    public double getProfit() {
        return this.profit;
    }

    public double getCurrentValue() {
        return this.currentValue;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPurchasedValue() {
        return this.purchasedValue;
    }

    public String getTickerSymbol() {
        return this.tickerSymbol;
    }

    @Override
    public void updateCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public void updateProfit() {
        this.profit = (this.currentValue - this.purchasedValue) * this.quantity;
    }

    public void setPurchasedValue(double purchasedValue) {
        this.purchasedValue = purchasedValue;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
