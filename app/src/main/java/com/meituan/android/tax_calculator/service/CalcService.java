package com.meituan.android.tax_calculator.service;

import com.google.gson.Gson;
import com.meituan.android.tax_calculator.model.CalcItem;
import com.meituan.android.tax_calculator.model.CalcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by duoshilin on 2019/3/15.
 * 计算个税的逻辑
 */
public class CalcService {

    private Map<String, List<Double>> config, oldCalcConfig, newCalcConfig;

    {
        initData();
    }

    private void initData() {
        Gson gson = new Gson();
        config = gson.fromJson("{\"begin\":[5000],\"level1\":[36000,0.03,0],\"level2\":[144000,0.1,2520],\"level3\":[300000,0.2,16920],\"level4\":[420000,0.25,31920],\"level5\":[660000,0.3,52920],\"level6\":[960000,0.35,85920],\"level7\":[null,0.45,181920]}", Map.class);
        oldCalcConfig = gson.fromJson("{\"begin\":[3500],\"level1\":[1500,0.03,0],\"level2\":[4500,0.1,105],\"level3\":[9000,0.2,555],\"level4\":[35000,0.25,1005],\"level5\":[55000,0.3,2755],\"level6\":[80000,0.35,5505],\"level7\":[null,0.45,13505]}", Map.class);
        newCalcConfig = gson.fromJson("{\"begin\":5000,\"level1\":[3000,0.03,0],\"level2\":[12000,0.1,210],\"level3\":[25000,0.2,1410],\"level4\":[35000,0.25,2660],\"level5\":[55000,0.3,4410],\"level6\":[80000,0.35,7160],\"level7\":[null,0.45,15160]}",Map.class);
//        System.out.println(oldCalcConfig.get("begin").get(0));
//        for (String key : oldCalcConfig.keySet()) {
//            System.out.println(key + " : " + oldCalcConfig.get(key));
//        }
    }

    public CalcResult calc(CalcItem calcItem){
        Tax result = calcData(calcItem);
        List<CalcResult.Item> detail = new ArrayList();
        detail.add(new CalcResult.Item("月份","税后收入","收入增加"));
        for (int i = 0; i < result.taxList.size(); i++) {
            double increased = result.taxList.get(i) - result.oldIncome;
            CalcResult.Item item = new CalcResult.Item((i+1)+"月",String.valueOf(result.taxList.get(i)),String.valueOf(increased));
            detail.add(item);
        }
        CalcResult.Item nzjItem = new CalcResult.Item("年终奖", String.valueOf(result.newActualAnnual),"0");
        detail.add(nzjItem);
        double increasedIncome = result.oldTax - result.newTax;
        return new CalcResult(increasedIncome,getRank(increasedIncome),result.newTax,result.oldTax,result.totalIncome,detail);
    }

    private String getRank(double increasedIncome) {
        String[] rankingPercent = {"0%", "60%", "65%", "70%", "72%", "75%", "80%", "85%", "90%", ""};

        if (increasedIncome < 20) {
            return rankingPercent[0];
        } else if (increasedIncome >= 20 && increasedIncome <= 100) {
            return rankingPercent[1];
        } else if (increasedIncome > 100 && increasedIncome <= 300) {
            return rankingPercent[2];
        } else if (increasedIncome > 300 && increasedIncome <= 500) {
            return rankingPercent[3];
        } else if (increasedIncome > 500 && increasedIncome <= 800) {
            return rankingPercent[4];
        } else if (increasedIncome > 800 && increasedIncome <= 1500) {
            return rankingPercent[5];
        } else if (increasedIncome > 1500 && increasedIncome <= 3000) {
            return rankingPercent[6];
        } else if (increasedIncome > 3000 && increasedIncome <= 5000) {
            return rankingPercent[7];
        } else if (increasedIncome > 5000) {
            return rankingPercent[8];
        } else {
            return rankingPercent[9];
        }
    }

    public Tax calcData(CalcItem calcItem) {
        // old calc ********
        double annual = calcItem.getNzj();
        double base = calcItem.getSalary() - oldCalcConfig.get("begin").get(0) - calcItem.getGjj() - calcItem.getSb();
        int level = calcRange(oldCalcConfig, base);
        double oldMonth = calcBasicByLevel(oldCalcConfig, level, base);
        System.out.println("salaryForTax："+ base);
        System.out.println("monthTax："+ oldMonth);
        double oldAnnual = calcBasicByLevel(oldCalcConfig, calcRange(oldCalcConfig, annual/12), annual);
        System.out.println("nzjForTax："+ annual);
        System.out.println("nzjTax："+ oldAnnual);
        double oldSalary = calcItem.getSalary() - oldMonth - calcItem.getGjj() - calcItem.getSb();
        if (oldSalary < 0){
            oldSalary = calcItem.getSalary();
        }
        double oldActualAnnual = annual - oldAnnual;
        System.out.println("oldSalary："+ oldSalary);
        System.out.println("actualAnnual："+ oldActualAnnual);
        double oldIncome = oldSalary * 12 + oldActualAnnual;
        double oldTotalTax = oldMonth * 12;

        // new calc *********
        NewTax result = calcNewDetailTax(calcItem.getSalary(), calcItem.getGjj(), calcItem.getSb(), calcItem.getSpecialDeducted());
        System.out.println("salary:" + calcItem.getSalary() + "totalTax:" + result.totalTax);
        double increaseSalary = oldTotalTax - result.totalTax;
//        increaseSalary < .01 && -.01 < increaseSalary && (increaseSalary = 0);
        if (increaseSalary > -.01 && increaseSalary< .01){
            increaseSalary = 0;
        }
        double newAnnual = calcBasicByLevel(newCalcConfig, calcRange(newCalcConfig, annual/12), annual);
        System.out.println("newAnnual: "+newAnnual);
        double newActualAnnual = annual - newAnnual;
        System.out.println("newActualAnnual: "+newActualAnnual);
        double totalIncome = newActualAnnual + result.totalMothIncome;
        System.out.println("totalIncome: "+totalIncome);
        return new Tax(oldTotalTax,result.totalTax,oldActualAnnual,newActualAnnual,increaseSalary,oldSalary,totalIncome,result.taxList);
   }

    private NewTax calcNewDetailTax(double salary, double gjj, double sb, double specialDeducted) {
        double baseSalary = Math.max(0, salary - gjj - sb - specialDeducted - config.get("begin").get(0));
        List<Double> taxList = new ArrayList<>();
        double totalMothIncome = 0;

        for (int i = 1; i <=12 ; i++) {
            int level = calcRange(config, i*baseSalary);
            if (i == 1){
                double tax = calcBasicByLevel(config, level, baseSalary);
                double monthIncome = salary - gjj - sb -tax;
                if (monthIncome < 0){
                    monthIncome = salary;
                }
                totalMothIncome +=monthIncome;
                taxList.add(monthIncome);
                continue;
            }
            int lastLevel = calcRange(config, (i-1)*baseSalary);
            double tax = calcBasicByLevel(config, level, i*baseSalary) - calcBasicByLevel(config,lastLevel,(i-1)*baseSalary);
            double monthIncome = salary - gjj - sb -tax;
            if (monthIncome < 0){
                monthIncome = salary;
            }
            totalMothIncome +=monthIncome;
            taxList.add(monthIncome);
        }
        int finalLevel = calcRange(config, 12 * baseSalary);
        double totalTax = calcBasicByLevel(config, finalLevel, 12*baseSalary);
        return new NewTax(taxList,totalTax,totalMothIncome);
    }

    private double calcBasicByLevel(Map<String, List<Double>> oldCalcConfig, int level, double base) {
        if (level == 0){
            return 0;
        }else {
            List<Double> l = oldCalcConfig.get("level"+level);
            return base * l.get(1) -l.get(2);
        }
    }

    /**
     * 通过应缴纳税额计算出当前的税率档次
     *
     * @param config 新旧税额配置
     * @param money  应缴纳税额款
     * @return
     */
    private int calcRange(Map<String, List<Double>> config, double money) {
        return money <= 0 ? 0 : money <= config.get("level1").get(0) ? 1 : money <= config.get("level2").get(0) ? 2 : money <= config.get("level3").get(0) ? 3 : money <= config.get("level4").get(0) ? 4 : money <= config.get("level5").get(0) ? 5 : money <= config.get("level6").get(0) ? 6 : 7;
    }


    public void main(String[] args) {
    }

    class NewTax{
        List<Double> taxList;
        double totalTax;
        double totalMothIncome;

        public NewTax(List<Double> taxList, double totalTax, double totalMothIncome) {
            this.taxList = taxList;
            this.totalTax = totalTax;
            this.totalMothIncome = totalMothIncome;
        }
    }

    class Tax{
        double oldTax,newTax, oldActualAnnual, newActualAnnual,increaseSalary, oldIncome, totalIncome;
        List<Double> taxList;

        public Tax(double oldTax, double newTax, double oldActualAnnual, double newActualAnnual, double increaseSalary, double oldIncome, double totalIncome, List<Double> taxList) {
            this.oldTax = oldTax;
            this.newTax = newTax;
            this.oldActualAnnual = oldActualAnnual;
            this.newActualAnnual = newActualAnnual;
            this.increaseSalary = increaseSalary;
            this.oldIncome = oldIncome;
            this.totalIncome = totalIncome;
            this.taxList = taxList;
        }
    }
    


}
