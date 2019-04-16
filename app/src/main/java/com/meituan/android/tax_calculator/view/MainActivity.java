package com.meituan.android.tax_calculator.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meituan.android.tax_calculator.R;
import com.meituan.android.tax_calculator.adapter.CutAdapter;
import com.meituan.android.tax_calculator.model.CalcItem;
import com.meituan.android.tax_calculator.model.CalcResult;
import com.meituan.android.tax_calculator.model.TaxInfo;
import com.meituan.android.tax_calculator.service.CalcService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.meituan.android.tax_calculator.utils.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private final String HEAD_BACKGROUND_IMAGE_URL = "https://p0.meituan.net/travelcube/5d508b7d3e165ed9ed96179c6694085d25400.png";

    private ImageView headerBackground, back;
    private LinearLayout cityLayout, gjjLayout, sbLayout;
    private LinearLayout gjjInfoLayout, gjjRatioLayout, sbInfoLayout;
    private TextView title, cityInput, gjjBaseTV, gjjRatioTV, gjjRatioInput, sbBaseTV, sbRatioTV, rule;
    private EditText salaryInput, gjjBaseInput, sbBaseInput, sbRatioInput, nzjInput;
    private ImageView gjjLayoutMore, sbLayoutMore;
    private RecyclerView sixDedectedRV;
    private Button calcBtn;
    private BottomSheetDialog dialog;
    private NumberPicker picker;
    private TextView pickerOk, pickerCancel;

    private String gjjBase, sbBase, gjjRatio, sbRatio;
    private Float gjjBaseStart, gjjBaseEnd, sbBaseStart, sbBaseEnd;
    private CutAdapter cutAdapter;
    private List<Map<String,Object>> cities;
    private CalcItem calcItem;
    private String[] gjjRatios = {"不缴纳公积金", "12.00%", "11.00%", "10.00%", "9.00%", "8.00%", "7.00%", "6.00%", "5.00%"};
    private String[] citiesName;
    private int gjjRatioSelectedIndex = 0, citySelectedIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcItem = new CalcItem();
        initViews();
        addListener();

        initCities();
        initDataByCityid(1);
        setHeaderBackground();

    }

    private void initCities(){
        cities = Util.initCities(this);
        Log.i(TAG, "initCities: "+cities);
        citiesName = new String[cities.size()-1];
        for (int i = 0; i < citiesName.length; i++) {
            citiesName[i] = cities.get(i).get("name").toString();
        }
    }

    private void initViews() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.bar_title);

        headerBackground = findViewById(R.id.header_background);

        salaryInput = findViewById(R.id.salary_input);
        cityInput = findViewById(R.id.city_input);
        cityLayout = findViewById(R.id.city_layout);
        gjjLayout = findViewById(R.id.gjj_layout);
        gjjInfoLayout = findViewById(R.id.gjj_info_layout);
        gjjLayoutMore = findViewById(R.id.gjj_layout_more);
        gjjBaseTV = findViewById(R.id.gjj_base_tv);
        gjjBaseInput = findViewById(R.id.gjj_base_input);
        gjjRatioTV = findViewById(R.id.gjj_ratio_tv);
        gjjRatioLayout = findViewById(R.id.gjj_ratio_layout);
        gjjRatioInput = findViewById(R.id.gjj_ratio_input);
        sbLayout = findViewById(R.id.sb_layout);
        sbInfoLayout = findViewById(R.id.sb_info_layout);
        sbLayoutMore = findViewById(R.id.sb_layout_more);
        sbBaseInput = findViewById(R.id.sb_base_input);
        sbBaseTV = findViewById(R.id.sb_base_tv);
        sbRatioInput = findViewById(R.id.sb_ratio_input);
        sbRatioTV = findViewById(R.id.sb_ratio_tv);
        nzjInput = findViewById(R.id.nzj_input);

        sixDedectedRV = findViewById(R.id.six_deducted_recycler_view);
        cutAdapter = new CutAdapter(this, Util.initCutList(getApplicationContext()));
        sixDedectedRV.setAdapter(cutAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        sixDedectedRV.setNestedScrollingEnabled(false);
        sixDedectedRV.setLayoutManager(manager);

        rule = findViewById(R.id.rule);

        calcBtn = findViewById(R.id.calc_btn);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.bottom_picker);
        picker = dialog.findViewById(R.id.picker);
        picker.setWrapSelectorWheel(false);
        Util.setDividerColor(picker, Color.parseColor("#3CC51F"));
        pickerOk = dialog.findViewById(R.id.ok);
        pickerCancel = dialog.findViewById(R.id.cancel);
    }

    private void addListener() {
        back.setOnClickListener(this);
        cityLayout.setOnClickListener(this);
        gjjLayout.setOnClickListener(this);
        gjjRatioLayout.setOnClickListener(this);
        sbLayout.setOnClickListener(this);
        salaryInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    float value = Float.parseFloat(s.toString());
                    if (value > 0) {
                        if (value < gjjBaseStart) {
                            gjjBaseInput.setText(gjjBaseStart.toString());
                        } else if (value > gjjBaseEnd) {
                            gjjBaseInput.setText(gjjBaseEnd.toString());
                        } else {
                            gjjBaseInput.setText(s);
                        }
                        if (value < sbBaseStart) {
                            sbBaseInput.setText(sbBaseStart.toString());
                        } else if (value > sbBaseEnd) {
                            sbBaseInput.setText(sbBaseEnd.toString());
                        } else {
                            sbBaseInput.setText(s);
                        }
                    }
                }
            }
        });
        gjjBaseInput.addTextChangedListener(new TextWatcher() {
            String beforeValue;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeValue = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    gjjBaseTV.setText(gjjBase);
                } else {
                    float value = Float.parseFloat(s.toString());
                    if (value > 100000) {
                        showErrorDialog("输入有误", "公积金基数请在0-100000内");
                        gjjBaseInput.setText(beforeValue);
                        gjjBaseInput.setSelection(start);
                        return;
                    }
                    if (value > 0) {
                        if (value < gjjBaseEnd) {
                            gjjBaseTV.setText("基数 ¥" + value);
                        } else {
                            gjjBaseTV.setText("基数 ¥" + gjjBaseEnd);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sbBaseInput.addTextChangedListener(new TextWatcher() {
            String beforeValue;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeValue = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    sbBaseTV.setText(sbBase);
                } else {
                    float value = Float.parseFloat(s.toString());
                    if (value > 100000) {
                        showErrorDialog("输入有误", "社保基数请在0-100000内");
                        sbBaseInput.setText(beforeValue);
                        sbBaseInput.setSelection(start);
                        return;
                    }
                    if (value > 0) {
                        if (value < sbBaseEnd)
                            sbBaseTV.setText("基数 ¥" + value);
                    } else {
                        sbBaseTV.setText("基数 ¥" + sbBaseEnd);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        sbRatioInput.addTextChangedListener(new TextWatcher() {
            String beforeValue;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeValue = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(s.toString());
                if (TextUtils.isEmpty(s)) {
                    sbRatioTV.setText("0.00%");
                } else {
                    float value = Float.parseFloat(s.toString());
                    if (value > 100) {
                        showErrorDialog("输入有误", "社保比率请在0-100内");
                        sbRatioInput.setText(beforeValue);
                        return;
                    } else if (value > 0) {
                        sbRatioTV.setText(Util.format("#.00", value) + "%");
                    }else if(value == 0){
                        sbRatioTV.setText("0.00%");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        rule.setOnClickListener(this);
        calcBtn.setOnClickListener(this);
        pickerCancel.setOnClickListener(this);

    }

    /**
     * 通过城市id加载基本信息
     *
     * @param cityid
     */
    private void initDataByCityid(int cityid) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://portal-portm.meituan.com/weapp/location/taxinfo?id=" + cityid)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "加载城市信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
                final TaxInfo taxInfo = new Gson().fromJson(jsonObject.get("data"), TaxInfo.class);
                gjjBase = "基数 ¥" + Util.format("#", taxInfo.getMinAccoumlationFundRadix()) + " - ¥" + Util.format("#", taxInfo.getMaxAccoumlationFundRadix());
                gjjBaseStart = Float.parseFloat(taxInfo.getMinAccoumlationFundRadix());
                gjjBaseEnd = Float.parseFloat(taxInfo.getMaxAccoumlationFundRadix());
                gjjRatio = Util.format("#.00", Float.parseFloat(taxInfo.getAccoumlationFundRate()) * 100) + "%";
                for (int i = 0; i < gjjRatios.length; i++) {
                    if (gjjRatio.equals(gjjRatios[i])) {
                        gjjRatioSelectedIndex = i;
                        break;
                    }
                }
                sbBase = "基数 ¥" + Util.format("#", taxInfo.getMinSocialRadix()) + " - ¥" + Util.format("#", taxInfo.getMaxSocialRadix());
                sbBaseStart = Float.parseFloat(taxInfo.getMinSocialRadix());
                sbBaseEnd = Float.parseFloat(taxInfo.getMaxSocialRadix());
                sbRatio = Util.format("#.00", (Float.parseFloat(taxInfo.getUnemploymentRate()) + 0.1f) * 100) + "%";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gjjBaseTV.setText(gjjBase);
                        gjjRatioTV.setText(gjjRatios[gjjRatioSelectedIndex]);
                        gjjRatioInput.setText(gjjRatios[gjjRatioSelectedIndex]);
                        sbBaseTV.setText(sbBase);
//                        sbRatioTV.setText(sbRatio);
                        sbRatioInput.setText(sbRatio.substring(0, sbRatio.indexOf("%")));
                        cityInput.setText(taxInfo.getCityName());
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.city_layout:
                //todo 跳转到选择城市的Activity页面，获取返回结果，重新调用 initData()
//                Toast.makeText(this, "获取地址", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClassName("com.sankuai.meituan","com.meituan.mmp.lib.HeraActivity");
//                startActivity(intent);
                picker.setDisplayedValues(citiesName);
                picker.setMaxValue(citiesName.length-1);
                picker.setValue(citySelectedIndex);
                pickerOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        citySelectedIndex = picker.getValue();
                        initDataByCityid((Double.valueOf(cities.get(citySelectedIndex).get("id").toString())).intValue());
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.gjj_layout:
                if (gjjInfoLayout.getVisibility() == View.VISIBLE) {
                    gjjInfoLayout.setVisibility(View.GONE);
                    gjjLayoutMore.setBackgroundResource(R.drawable.ic_down);
                } else {
                    gjjInfoLayout.setVisibility(View.VISIBLE);
                    gjjLayoutMore.setBackgroundResource(R.drawable.ic_up);
                }
                break;
            case R.id.gjj_ratio_layout:
                picker.setDisplayedValues(gjjRatios);
                picker.setMaxValue(gjjRatios.length-1);
                picker.setValue(gjjRatioSelectedIndex);
                pickerOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gjjRatioSelectedIndex = picker.getValue();
                        if (gjjRatioSelectedIndex == 0){
                            gjjRatioTV.setText("0.00%");
                        }else {
                            gjjRatioTV.setText(gjjRatios[gjjRatioSelectedIndex]);
                        }
                        gjjRatioInput.setText(gjjRatios[gjjRatioSelectedIndex]);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.sb_layout:
                if (sbInfoLayout.getVisibility() == View.VISIBLE) {
                    sbInfoLayout.setVisibility(View.GONE);
                    sbLayoutMore.setBackgroundResource(R.drawable.ic_down);
                } else {
                    sbInfoLayout.setVisibility(View.VISIBLE);
                    sbLayoutMore.setBackgroundResource(R.drawable.ic_up);
                }
                break;
            case R.id.rule:
                startActivity(new Intent(MainActivity.this, RuleActivity.class));
                break;
            case R.id.calc_btn:
                calc();
//                startActivity(new Intent(MainActivity.this, ResultActivity.class));
                break;
            case R.id.cancel:
                if (dialog!=null){
                    dialog.dismiss();
                }
                break;
            default:
        }
    }

    /**
     * 设置header的背景图片
     */
    private void setHeaderBackground() {
        Picasso.with(this).load(HEAD_BACKGROUND_IMAGE_URL).into(headerBackground);
    }

    private void showErrorDialog(String title, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .setCancelable(false)
                .show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3CC51F"));
    }

    /**
     * 点击计算按钮时，获取所需数据，进行计算
     *
     * @return
     */
    private void calc() {
        float salary = formatCalcItem(salaryInput.getText().toString());
        if (salary < 1 || salary > 500000) {
            showErrorDialog("请注意", "税前工资请在1-500000范围内");
            return;
        }
        float nzj = formatCalcItem(nzjInput.getText().toString());
        if (nzj < 0 || nzj > 5000000) {
            showErrorDialog("请注意", "年终奖输入范围为1-5000000元");
            return;
        }
        calcItem.setSalary(salary)
                .setGjjBase(formatCalcItem(gjjBaseInput.getText().toString()))
                .setGjjRatio(formatCalcItem(gjjRatioTV.getText().toString()))
                .setSbBase(formatCalcItem(sbBaseInput.getText().toString()))
                .setSbRatio(formatCalcItem(sbRatioTV.getText().toString()))
                .setNzj(formatCalcItem(nzjInput.getText().toString()))
                .setDecuted1(formatCalcItem(((TextView) sixDedectedRV.getChildAt(0).findViewById(R.id.deducted_value)).getText().toString()))
                .setDecuted2(formatCalcItem(((TextView) sixDedectedRV.getChildAt(1).findViewById(R.id.deducted_value)).getText().toString()))
                .setDecuted3(formatCalcItem(((TextView) sixDedectedRV.getChildAt(2).findViewById(R.id.deducted_value)).getText().toString()))
                .setDecuted4(formatCalcItem(((TextView) sixDedectedRV.getChildAt(3).findViewById(R.id.deducted_value)).getText().toString()))
                .setDecuted5(formatCalcItem(((TextView) sixDedectedRV.getChildAt(4).findViewById(R.id.deducted_value)).getText().toString()));
        Log.i(TAG, "calc: " + calcItem);

        //todo 通过一定的计算公式获取计算结果
        CalcResult result = new CalcService().calc(calcItem);

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("result", new Gson().toJson(result));
        startActivity(intent);
    }

    /**
     * 格式化计算项数据
     * 1、为空，返回0
     * 2、包含"¥"符号的去掉，转换为float类型
     * 3、包含"%"符号的，转换为float类型
     * 4、其他字符串类型的数字转换为float返回
     * 5、转换异常返回0
     *
     * @param value
     * @return
     */
    private float formatCalcItem(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        try {
            float result = 0;
            if (value.indexOf("¥ ") != -1) {
                value = value.substring(value.indexOf("¥ ") + 2, value.length());
                result = Float.parseFloat(value);
            } else if (value.indexOf("%") != -1) {
                value = value.substring(0, value.indexOf("%"));
                result = Float.parseFloat(value) / 100f;
//                result = Float.parseFloat(value) / 100f;
            }else {
                result = Float.parseFloat(value);
            }
            return result;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
