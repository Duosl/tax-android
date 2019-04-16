package com.meituan.android.tax_calculator.model;


/**
 * Created by duoshilin on 2019/3/4.
 */

public class TaxInfo {
    private String cityid;
    private String cityInitial;
    private String cityName;
    private String accoumlationFundRate;
    private String endowmentRate;
    private String maxAccoumlationFundRadix;
    private String maxSocialRadix;
    private String medicalRate;
    private String minAccoumlationFundRadix;
    private String minSocialRadix;
    private String unemploymentRate;

    public String getCityid() {
        return cityid;
    }

    public String getCityInitial() {
        return cityInitial;
    }

    public String getCityName() {
        return cityName;
    }

    public String getAccoumlationFundRate() {
        return accoumlationFundRate;
    }

    public String getEndowmentRate() {
        return endowmentRate;
    }

    public String getMaxAccoumlationFundRadix() {
        return maxAccoumlationFundRadix;
    }

    public String getMaxSocialRadix() {
        return maxSocialRadix;
    }

    public String getMedicalRate() {
        return medicalRate;
    }

    public String getMinAccoumlationFundRadix() {
        return minAccoumlationFundRadix;
    }

    public String getMinSocialRadix() {
        return minSocialRadix;
    }

    public String getUnemploymentRate() {
        return unemploymentRate;
    }
}
