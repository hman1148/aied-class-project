package com.ai_tutor.Models;

public class Answer {
    private String option;
    private double cost;
    private String explanation;

    public Answer(String option, String explanation ,double cost) {
        this.option = option;
        this.cost = cost;
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
