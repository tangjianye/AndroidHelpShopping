package com.product.helpshopping.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.product.common.utils.LogUtils;
import com.product.helpshopping.R;
import com.product.helpshopping.ui.base.AppBaseActivity;
import com.product.helpshopping.ui.fragment.CategoryFragment;
import com.product.helpshopping.ui.fragment.GuideFragment;
import com.product.helpshopping.ui.fragment.HomeFragment;
import com.product.helpshopping.ui.fragment.PersonalCenterFragment;

public class MainActivity extends AppBaseActivity implements TabHost.OnTabChangeListener {
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.contentLayout);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setOnTabChangedListener(this);
        initTab();

    }

    private void initTab() {
        TabSpec tabSpec = null;

        tabSpec = tabHost.newTabSpec(getString(R.string.label_home))
                .setIndicator(getTabView(R.string.label_home));
        tabHost.addTab(tabSpec, HomeFragment.class, null);
        tabHost.setTag(getString(R.string.label_home));

        tabSpec = tabHost.newTabSpec(getString(R.string.label_category))
                .setIndicator(getTabView(R.string.label_category));
        tabHost.addTab(tabSpec, CategoryFragment.class, null);
        tabHost.setTag(getString(R.string.label_category));

        tabSpec = tabHost.newTabSpec(getString(R.string.label_guide))
                .setIndicator(getTabView(R.string.label_guide));
        tabHost.addTab(tabSpec, GuideFragment.class, null);
        tabHost.setTag(getString(R.string.label_guide));

        tabSpec = tabHost.newTabSpec(getString(R.string.label_personal_center))
                .setIndicator(getTabView(R.string.label_personal_center));
        tabHost.addTab(tabSpec, PersonalCenterFragment.class, null);
        tabHost.setTag(getString(R.string.label_personal_center));
    }

    private View getTabView(int contentId) {
        View view = LayoutInflater.from(this).inflate(R.layout.footer_tabs, null);
//        ((TextView) view.findViewById(R.id.tvTab)).setText(TabDb.getTabsTxt()[idx]);
//        if (idx == 0) {
//            ((TextView) view.findViewById(R.id.tvTab)).setTextColor(Color.RED);
//            ((ImageView) view.findViewById(R.id.ivImg)).setImageResource(TabDb.getTabsImgLight()[idx]);
//        } else {
//            ((ImageView) view.findViewById(R.id.ivImg)).setImageResource(TabDb.getTabsImg()[idx]);
//        }

        ((TextView) view.findViewById(R.id.tvTab)).setText(contentId);
        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        LogUtils.i("tangjy", "onTabChanged tabId = " + tabId);
//        updateTab();
    }

//    private void updateTab() {
//        TabWidget tabw = tabHost.getTabWidget();
//        for (int i = 0; i < tabw.getChildCount(); i++) {
//            View view = tabw.getChildAt(i);
//            ImageView iv = (ImageView) view.findViewById(R.id.ivImg);
//            if (i == tabHost.getCurrentTab()) {
//                ((TextView) view.findViewById(R.id.tvTab)).setTextColor(Color.RED);
//                iv.setImageResource(TabDb.getTabsImgLight()[i]);
//            } else {
//                ((TextView) view.findViewById(R.id.tvTab)).setTextColor(getResources().getColor(R.color.foot_txt_gray));
//                iv.setImageResource(TabDb.getTabsImg()[i]);
//            }
//
//        }
//    }
}
