package com.meituan.android.tax_calculator.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.meituan.android.tax_calculator.R;

/**
 * Created by duoshilin on 2019/3/7.
 */
public class ActionBarFragment extends Fragment {

    private ImageView backIcon;
    private TextView textView;
    private FrameLayout actionBarFragmentLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.action_bar_fragment,container,false);
        backIcon = view.findViewById(R.id.back);
        textView = view.findViewById(R.id.bar_title);
        actionBarFragmentLayout = view.findViewById(R.id.action_bar_fragment_layout);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    public void setBackIcon(int resid) {
        this.backIcon.setBackgroundResource(resid);
    }

    public TextView getTitle() {
        return textView;
    }

    public FrameLayout getActionBarFragmentLayout() {
        return actionBarFragmentLayout;
    }
}
