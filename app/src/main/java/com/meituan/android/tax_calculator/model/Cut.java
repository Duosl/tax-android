package com.meituan.android.tax_calculator.model;

import java.util.List;

/**
 * Created by duoshilin on 2019/3/4.
 */
public class Cut {
    private String title;
    private String action;
    private String resultMoney;
    private boolean hasChildView;
    private String prefix;

    private List<Child> childList;

    public String getTitle() {
        return title;
    }

    public String getAction() {
        return action;
    }

    public String getResultMoney() {
        return resultMoney;
    }

    public boolean hasChildView() {
        return hasChildView;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public class Child {
        private String title;
        private String payload;
        private String money;

        @Override
        public String toString() {
            return "Child{" +
                    "title='" + title + '\'' +
                    ", payload='" + payload + '\'' +
                    ", money='" + money + '\'' +
                    '}';
        }

        public String getTitle() {
            return title;
        }

        public String getPayload() {
            return payload;
        }

        public String getMoney() {
            return money;
        }
    }

    @Override
    public String toString() {
        return "Cut{" +
                "title='" + title + '\'' +
                ", action='" + action + '\'' +
                ", resultMoney='" + resultMoney + '\'' +
                ", hasChildView=" + hasChildView +
                ", prefix='" + prefix + '\'' +
                ", childList=" + childList +
                '}';
    }

}
