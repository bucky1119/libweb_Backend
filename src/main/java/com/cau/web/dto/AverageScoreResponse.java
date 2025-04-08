package com.cau.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AverageScoreResponse {
    @JsonProperty("averageInnovationScore")
    private double averageInnovationScore;
    @JsonProperty("averageDisruptionScore")
    private double averageDisruptionScore;
    @JsonProperty("averageFrontierScore")
    private double averageFrontierScore;
    @JsonProperty("averageIndustryImpactScore")
    private double averageIndustryImpactScore;
    @JsonProperty("averageAdditionalScore")
    private double averageAdditionalScore;
    @JsonProperty("averageValueScore")
    private double averageValueScore;

    // 无参构造函数
    public AverageScoreResponse() {
    }

    // 带参数的构造函数
    public AverageScoreResponse(double averageInnovationScore, double averageDisruptionScore, double averageFrontierScore, double averageIndustryImpactScore, double averageAdditionalScore, double averageValueScore) {
        this.averageInnovationScore = averageInnovationScore;
        this.averageDisruptionScore = averageDisruptionScore;
        this.averageFrontierScore = averageFrontierScore;
        this.averageIndustryImpactScore = averageIndustryImpactScore;
        this.averageAdditionalScore = averageAdditionalScore;
        this.averageValueScore = averageValueScore;
    }

    // Getters 和 Setters

    public double getAverageInnovationScore() {
        return averageInnovationScore;
    }

    public double getAverageDisruptionScore() {
        return averageDisruptionScore;
    }

    public double getAverageFrontierScore() {
        return averageFrontierScore;
    }

    public double getAverageIndustryImpactScore() {
        return averageIndustryImpactScore;
    }

    public double getAverageAdditionalScore() {
        return averageAdditionalScore;
    }

    public double getAverageValueScore() {
        return averageValueScore;
    }

    public void setAverageInnovationScore(double averageInnovationScore) {
        this.averageInnovationScore = averageInnovationScore;
    }

    public void setAverageDisruptionScore(double averageDisruptionScore) {
        this.averageDisruptionScore = averageDisruptionScore;
    }

    public void setAverageFrontierScore(double averageFrontierScore) {
        this.averageFrontierScore = averageFrontierScore;
    }

    public void setAverageIndustryImpactScore(double averageIndustryImpactScore) {
        this.averageIndustryImpactScore = averageIndustryImpactScore;
    }

    public void setAverageAdditionalScore(double averageAdditionalScore) {
        this.averageAdditionalScore = averageAdditionalScore;
    }

    public void setAverageValueScore(double averageValueScore) {
        this.averageValueScore = averageValueScore;
    }
}
