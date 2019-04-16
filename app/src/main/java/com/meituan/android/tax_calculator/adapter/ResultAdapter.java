package com.meituan.android.tax_calculator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meituan.android.tax_calculator.R;
import com.meituan.android.tax_calculator.model.CalcResult;
import com.meituan.android.tax_calculator.utils.Util;

import java.util.List;

/**
 * Created by duoshilin on 2019/3/17.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private Context context;
    private List<CalcResult.Item> datas;

    public ResultAdapter(Context context, List<CalcResult.Item> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_table_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.title.setBackground(context.getResources().getDrawable(R.drawable.textview_boder2));
            holder.income.setBackground(context.getResources().getDrawable(R.drawable.textview_boder2));
            holder.increased.setBackground(context.getResources().getDrawable(R.drawable.textview_boder2));
            holder.title.setText(datas.get(position).getMonth());
            holder.income.setText(datas.get(position).getIncome());
            holder.increased.setText(datas.get(position).getIncreased());
            return;
        } else if (position == datas.size() - 1) {
            holder.title.setBackground(context.getResources().getDrawable(R.drawable.textview_boder2));

        }
        float income = Float.parseFloat(datas.get(position).getIncome());
        float increased = Float.parseFloat(datas.get(position).getIncreased());
        holder.title.setText(datas.get(position).getMonth());
        holder.income.setText(income==0 ? "0.00" : Util.format("#.00", income));
        holder.increased.setText(increased==0 ? "0.00" : Util.format("#.00", increased));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, income, increased;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.month);
            income = view.findViewById(R.id.income);
            increased = view.findViewById(R.id.increased);
        }
    }
}
