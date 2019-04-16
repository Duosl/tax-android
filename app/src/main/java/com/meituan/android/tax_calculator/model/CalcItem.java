package com.meituan.android.tax_calculator.model;

/**
 * Created by duoshilin on 2019/3/11.
 */
public class CalcItem {

    private float salary;
    private float nzj;
    private float gjjBase;
    private float gjjRatio;
    private float sbBase;
    private float sbRatio;

    private float decuted1;
    private float decuted2;
    private float decuted3;
    private float decuted4;
    private float decuted5;

    private float gjj;
    private float sb;
    private float specialDeducted;

    public float getSalary() {
        return salary;
    }

    public CalcItem setSalary(float salary) {
        this.salary = salary;
        return this;
    }

    public float getNzj() {
        return nzj;
    }

    public CalcItem setNzj(float nzj) {
        this.nzj = nzj;
        return this;
    }

    public float getGjjBase() {
        return gjjBase;
    }

    public CalcItem setGjjBase(float gjjBase) {
        this.gjjBase = gjjBase;
        return this;
    }

    public float getGjjRatio() {
        return gjjRatio;
    }

    public CalcItem setGjjRatio(float gjjRatio) {
        this.gjjRatio = gjjRatio;
        return this;
    }

    public float getSbBase() {
        return sbBase;
    }

    public CalcItem setSbBase(float sbBase) {
        this.sbBase = sbBase;
        return this;
    }

    public float getSbRatio() {
        return sbRatio;
    }

    public CalcItem setSbRatio(float sbRatio) {
        this.sbRatio = sbRatio;
        return this;
    }

    public float getDecuted1() {
        return decuted1;
    }

    public CalcItem setDecuted1(float decuted1) {
        this.decuted1 = decuted1;
        return this;
    }

    public float getDecuted2() {
        return decuted2;
    }

    public CalcItem setDecuted2(float decuted2) {
        this.decuted2 = decuted2;
        return this;
    }

    public float getDecuted3() {
        return decuted3;
    }

    public CalcItem setDecuted3(float decuted3) {
        this.decuted3 = decuted3;
        return this;
    }

    public float getDecuted4() {
        return decuted4;
    }

    public CalcItem setDecuted4(float decuted4) {
        this.decuted4 = decuted4;
        return this;
    }

    public float getDecuted5() {
        return decuted5;
    }

    public CalcItem setDecuted5(float decuted5) {
        this.decuted5 = decuted5;
        return this;
    }

    public float getGjj() {
        return gjjBase*gjjRatio;
    }

    public float getSb() {
        return sbBase*sbRatio;
    }

    public float getSpecialDeducted() {
        return decuted1+decuted2+decuted3+decuted4+decuted5;
    }

    @Override
    public String toString() {
        return "{" +
                "salary : " + salary +
                ", nzj : " + nzj +
                ", gjjBase : " + gjjBase +
                ", gjjRatio : " + gjjRatio +
                ", sbBase : " + sbBase +
                ", sbRatio : " + sbRatio +
                ", decuted1 : " + decuted1 +
                ", decuted2 : " + decuted2 +
                ", decuted3 : " + decuted3 +
                ", decuted4 : " + decuted4 +
                ", decuted5 : " + decuted5 +
                '}';
    }
}
