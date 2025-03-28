package com.ai_tutor.Models;

public abstract class InvestmentAbstract {

    public double purchasedValue;
    public String name;
    public double currentValue;
    public int quantity;
    public double profit;

    public InvestmentAbstract(String name, double purchasedValue, int quantity) {
        this.name = name;
        this.purchasedValue = purchasedValue;
        this.quantity = quantity;
        this.currentValue = purchasedValue;
        this.profit = 0;
    }

    public abstract void updateCurrentValue(double currentValue);
    public abstract void updateProfit();
}
