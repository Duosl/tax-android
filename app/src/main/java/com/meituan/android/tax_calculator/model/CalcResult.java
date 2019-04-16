package com.meituan.android.tax_calculator.model;

import java.util.List;

/**
 * Created by duoshilin on 2019/3/16.
 */
public class CalcResult {

    private double increasedIncome;
    private String rankingPercent;
    private double totalNewTaxAmount;
    private double totalOldTaxAmount;
    private double annualInCome;
    private List<Item> annualDetail;

    public CalcResult(double increasedIncome, String rankingPercent, double totalNewTaxAmount, double totalOldTaxAmount, double annualInCome, List<Item> annualDetail) {
        this.increasedIncome = increasedIncome;
        this.rankingPercent = rankingPercent;
        this.totalNewTaxAmount = totalNewTaxAmount;
        this.totalOldTaxAmount = totalOldTaxAmount;
        this.annualInCome = annualInCome;
        this.annualDetail = annualDetail;
    }

    public double getIncreasedIncome() {
        return increasedIncome;
    }

    public String getRankingPercent() {
        return rankingPercent;
    }

    public double getTotalNewTaxAmount() {
        return totalNewTaxAmount;
    }

    public double getTotalOldTaxAmount() {
        return totalOldTaxAmount;
    }

    public double getAnnualInCome() {
        return annualInCome;
    }

    public List<Item> getAnnualDetail() {
        return annualDetail;
    }

    public static class Item{
        private String month,income, increased;

        public Item(String month, String income, String increased) {
            this.month = month;
            this.income = income;
            this.increased = increased;
        }

        public String getMonth() {
            return month;
        }

        public String getIncome() {
            return income;
        }

        public String getIncreased() {
            return increased;
        }
    }
}
