package com.meituan.android.tax_calculator.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.meituan.android.tax_calculator.R;
import com.meituan.android.tax_calculator.model.Cut;
import com.meituan.android.tax_calculator.utils.Util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by duoshilin on 2019/3/4.
 */
public class CutAdapter extends RecyclerView.Adapter<CutAdapter.ViewHolder> {
    private static final String TAG = "CutAdapter";

    private static List<Cut> cutList ;
    private Context context;
    private ItemStatus[] itemStatuses;

    public ItemStatus[] getItemStatuses() {
        return itemStatuses;
    }

    public CutAdapter(Context context, List<Cut> cutList) {
        this.context = context;
        this.cutList = cutList;
        itemStatuses = new ItemStatus[cutList.size()];
    }

    @Override
    public CutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.special_layout_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Cut cut = cutList.get(position);
        holder.deductedRadio.setText(cut.getTitle());
        if (cut.hasChildView()) {
            if (cut.getPrefix()!=null){
                holder.deducteTitle.setText(cut.getPrefix());
                holder.deductedInput.setText(cut.getChildList().get(0).getTitle());
            }else {
                holder.deducteTitle.setText(cut.getChildList().get(0).getTitle());
            }
        }
        //初始化Picker
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(R.layout.bottom_picker);
        final NumberPicker picker = dialog.findViewById(R.id.picker);
        Util.setDividerColor(picker, Color.parseColor("#3CC51F"));
        picker.setWrapSelectorWheel(false);
        final TextView pickerOk = dialog.findViewById(R.id.ok);
        TextView pickerCancel = dialog.findViewById(R.id.cancel);
        pickerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //显示或隐藏子布局
        holder.deductedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.deductedRadio.isChecked()) {
                    holder.deductedRadio.setChecked(false);
                    holder.deductedValue.setText("");
                    if (cut.hasChildView()) {
                        holder.deductedInfoLayout.setVisibility(View.GONE);
                    }
                } else {
                    if (itemStatuses[4] != null && itemStatuses[4].isChecked && position == 3 ){
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("请注意")
                                .setMessage("您已选择租房专项扣除，不能同时享受房贷专项扣除哦！")
                                .setPositiveButton("确定",null)
                                .setCancelable(false)
                                .show();
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3CC51F"));
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(16f);
                        return;
                    }else if(itemStatuses[3] != null && itemStatuses[3].isChecked && position == 4){
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("请注意")
                                .setMessage("您已选择房贷专项扣除，不能同时享受租房专项扣除哦！")
                                .setPositiveButton("确定",null)
                                .setCancelable(false)
                                .show();
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3CC51F"));
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(16f);
                        return;
                    }
                    holder.deductedRadio.setChecked(true);
                    if (cut.hasChildView()) {
                        holder.deductedInfoLayout.setVisibility(View.VISIBLE);
                        holder.deductedValue.setText("¥ "+cut.getChildList().get(0).getMoney());
                    }else {
                        holder.deductedValue.setText("¥ "+cut.getResultMoney());
                    }
                }

                itemStatuses[position] =  new ItemStatus(holder.deductedRadio.isChecked(),holder.deductedValue.getText().toString());
                Log.i(TAG, "onClick: "+ Arrays.toString(itemStatuses));
            }
        });

        //点击子布局显示单选列表
        holder.deductedInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Cut.Child> childs = cut.getChildList();
//                Log.i(TAG, childs.toString());
                final String[] items = new String[childs.size()];
                for (int i = 0; i < childs.size(); i++) {
                    items[i] = childs.get(i).getTitle();
                }
                Log.i(TAG, "["+position+"].length = " + items.length);
                Log.i(TAG, String.valueOf(picker.getMaxValue()));
                picker.setMaxValue(items.length-1);
                picker.setDisplayedValues(items);
                picker.setValue(itemStatuses[position].selectedIndex);
                pickerOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = picker.getValue();
                        itemStatuses[position].setValue(childs.get(index).getMoney());
                        itemStatuses[position].setSelectedIndex(index);
                        holder.deductedValue.setText("¥ "+itemStatuses[position].getValue());
                        if (cut.getPrefix()!=null){
                            holder.deductedInput.setText(childs.get(itemStatuses[position].getSelectedIndex()).getTitle());
                        }else {
                            holder.deducteTitle.setText(childs.get(itemStatuses[position].getSelectedIndex()).getTitle());
                        }
                        itemStatuses[position].selectedIndex = index;
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cutList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout deductedLayout, deductedInfoLayout;
        RadioButton deductedRadio;
        TextView deducteTitle, deductedValue, deductedInput;


        public ViewHolder(View view) {
            super(view);
            deductedLayout = view.findViewById(R.id.deducted_layout);
            deductedInfoLayout = view.findViewById(R.id.deducted_info_layout);
            deductedRadio = view.findViewById(R.id.deducted_radio);
            deducteTitle = view.findViewById(R.id.deducted_title);
            deductedValue = view.findViewById(R.id.deducted_value);
            deductedInput = view.findViewById(R.id.deducted_input);
        }
    }

    public class ItemStatus {
        boolean isChecked = false;
        int selectedIndex = 0;
        String value;

        public ItemStatus(boolean isChecked, String value) {
            this.isChecked = isChecked;
            System.out.println(value);
            if (value.indexOf("¥ ") != -1){
                this.value = value.substring(value.indexOf("¥ ")+2, value.length());
            }else {
                this.value = value;
            }

        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        @Override
        public String toString() {
            return "{" +
                    "isChecked=" + isChecked +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

}
