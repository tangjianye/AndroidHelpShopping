package com.product.helpshopping.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.product.common.utils.LogUtils;
import com.product.helpshopping.BaseApplication;
import com.product.helpshopping.thridpart.push.PushProxy;
import com.product.helpshopping.thridpart.update.UpdateProxy;
import com.product.helpshopping.ui.base.BaseActivity;
import com.product.helpshopping.ui.fragment.CategoryFragment;
import com.product.helpshopping.ui.fragment.GuideFragment;
import com.product.helpshopping.ui.fragment.HomeFragment;
import com.product.helpshopping.ui.fragment.PersonalCenterFragment;
import com.product.helpshopping.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainShoppingActivity extends BaseActivity implements TabHost.OnTabChangeListener {
    private static final String TAG = MainShoppingActivity.class.getSimpleName();

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.product.helpshopping.R.layout.activity_shopping_main);
        ButterKnife.bind(this);

        /** 初始化文件夹等环境 */
        CommonUtils.initAppEnvironment();
        /** 应用升级 */
        UpdateProxy.getInstance().update(this);
        /** 百度push */
        PushProxy.getInstance().startWork(this);

        initView();
        initTab();
    }

    @Override
    public void onResume() {
        super.onResume();
        // MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    private void initView() {
        mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), com.product.helpshopping.R.id.contentLayout);
        mTabhost.getTabWidget().setDividerDrawable(null);
        mTabhost.setOnTabChangedListener(this);
    }

    private void initTab() {
        TabSpec tabSpec = null;

        tabSpec = mTabhost.newTabSpec(getString(com.product.helpshopping.R.string.label_home))
                .setIndicator(getTabView(com.product.helpshopping.R.string.label_home, com.product.helpshopping.R.drawable.selector_tab_home));
        mTabhost.addTab(tabSpec, HomeFragment.class, null);
        mTabhost.setTag(getString(com.product.helpshopping.R.string.label_home));

        tabSpec = mTabhost.newTabSpec(getString(com.product.helpshopping.R.string.label_category))
                .setIndicator(getTabView(com.product.helpshopping.R.string.label_category, com.product.helpshopping.R.drawable.selector_tab_category));
        mTabhost.addTab(tabSpec, CategoryFragment.class, null);
        mTabhost.setTag(getString(com.product.helpshopping.R.string.label_category));

        tabSpec = mTabhost.newTabSpec(getString(com.product.helpshopping.R.string.label_guide))
                .setIndicator(getTabView(com.product.helpshopping.R.string.label_guide, com.product.helpshopping.R.drawable.selector_tab_guide));
        mTabhost.addTab(tabSpec, GuideFragment.class, null);
        mTabhost.setTag(getString(com.product.helpshopping.R.string.label_guide));

        tabSpec = mTabhost.newTabSpec(getString(com.product.helpshopping.R.string.label_personal_center))
                .setIndicator(getTabView(com.product.helpshopping.R.string.label_personal_center, com.product.helpshopping.R.drawable.selector_tab_personl));
        mTabhost.addTab(tabSpec, PersonalCenterFragment.class, null);
        mTabhost.setTag(getString(com.product.helpshopping.R.string.label_personal_center));
    }

    private View getTabView(int contentId, int iconId) {
        View view = LayoutInflater.from(this).inflate(com.product.helpshopping.R.layout.include_footer_tabs, null);
        ((TextView) view.findViewById(com.product.helpshopping.R.id.tvTab)).setText(contentId);
        ((ImageView) view.findViewById(com.product.helpshopping.R.id.ivImg)).setImageResource(iconId);
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        LogUtils.i(TAG, "onTabChanged tabId = " + tabId);
        MobclickAgent.onEvent(this, "click", tabId);
    }

    private static final long EXIT_INTERVAL = 2000L;
    private long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > EXIT_INTERVAL) {
                showToast(com.product.helpshopping.R.string.exit_app);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                ((BaseApplication) getApplicationContext()).exitApp(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
