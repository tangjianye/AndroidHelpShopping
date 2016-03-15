package com.product.helpshopping.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.product.helpshopping.R;
import com.product.helpshopping.ui.base.AppBaseFragment;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class GuideFragment extends AppBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        TextView view = new TextView(getAppBaseActivity());
        view.setText(R.string.label_guide);
        view.setTextColor(Color.parseColor("#ff0000"));
        return view;
    }
}
