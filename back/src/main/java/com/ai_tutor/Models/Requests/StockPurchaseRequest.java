package com.ai_tutor.Models.Requests;

public class StockPurchaseRequest {

    private String tickerSymbol;
    private int quantity;

    public StockPurchaseRequest(String tickerSymbol, int quantity) {
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
