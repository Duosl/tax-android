package com.meituan.android.tax_calculator.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.meituan.android.tax_calculator.R;
import com.meituan.android.tax_calculator.adapter.ResultAdapter;
import com.meituan.android.tax_calculator.model.CalcResult;

public class ResultActivity extends AppCompatActivity {

    private TextView addIncomeTV, overTV, newTaxTV, oldTaxTV, yearIncomeTV;
    private CalcResult calcResult;
    private RecyclerView resultTableRv;
    private Button shareBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String result = getIntent().getStringExtra("result");
        System.out.println(result);
        calcResult = new Gson().fromJson(result,CalcResult.class);

        addIncomeTV = findViewById(R.id.year_add_income_tv);
        overTV = findViewById(R.id.over_tv);
        newTaxTV = findViewById(R.id.new_tax_tv);
        oldTaxTV = findViewById(R.id.old_tax_tv);
        yearIncomeTV = findViewById(R.id.year_income_tv);
        ((ActionBarFragment) getFragmentManager().findFragmentByTag("action_bar_fragment")).getTitle().setText("");

        resultTableRv = findViewById(R.id.result_table_rv);
        resultTableRv.setLayoutManager(new LinearLayoutManager(this));
        resultTableRv.setAdapter(new ResultAdapter(this,calcResult.getAnnualDetail()));
        resultTableRv.setNestedScrollingEnabled(false);

        shareBtn = findViewById(R.id.result_share);
        shareBtn.bringToFront();
        initData();
    }

    private void initData() {
        RelativeSizeSpan bigerSizeSpan = new RelativeSizeSpan(2.0f);
        SpannableString incomeSpan = new SpannableString(String.format(getString(R.string.income),calcResult.getIncreasedIncome()));
        incomeSpan.setSpan(bigerSizeSpan,1,incomeSpan.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        addIncomeTV.setText(incomeSpan);

        SpannableString overSpan = new SpannableString(String.format(getString(R.string.over),calcResult.getRankingPercent(),"多一次带父母出去玩的机会","陪伴是爱最好的表达"));
        overSpan.setSpan(bigerSizeSpan,5,8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        overSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#FFE24C")),5,8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        overTV.setText(overSpan);
        newTaxTV.setText(String.format(getString(R.string.income),calcResult.getTotalNewTaxAmount()));
        oldTaxTV.setText(String.format(getString(R.string.income),calcResult.getTotalOldTaxAmount()));
        yearIncomeTV.setText(String.format(getString(R.string.income),calcResult.getAnnualInCome()));
    }
}
