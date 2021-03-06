package com.product.helpshopping.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.product.helpshopping.R;
import com.product.helpshopping.common.interfaces.IInit;
import com.product.helpshopping.ui.adapter.BaseFragmentPagerAdapter;
import com.product.helpshopping.ui.base.BaseActivity;
import com.product.helpshopping.ui.fragment.CommunityFragment;
import com.product.helpshopping.ui.fragment.ReadFragment;
import com.product.helpshopping.ui.view.ScrollViewPager;

import java.util.ArrayList;

public class CommunityActivity extends BaseActivity implements IInit, View.OnClickListener {
    private static final String TAG = CommunityActivity.class.getSimpleName();
    private static final int SELECTED_ITEM_INDEX = 0;
    /**
     * 标题选择
     */
    private TextView mCommunityView;
    private TextView mReadView;

    private ScrollViewPager mViewPager;
    private BaseFragmentPagerAdapter mPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        initDatas();
        initTitles();
        initViews();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initTitles() {
        ImageView mTitleBack = (ImageView) findViewById(R.id.ic_header);
        ImageView mTitleMore = (ImageView) findViewById(R.id.more_header);
        //mTitleTips.setText(R.string.app_name);
        mTitleMore.setVisibility(View.GONE);

        mTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED, null);
                finish();
            }
        });
    }

    @Override
    public void initViews() {
        // 标题
        mCommunityView = (TextView) findViewById(R.id.title_community);
        mReadView = (TextView) findViewById(R.id.title_read);
        mViewPager = (ScrollViewPager) findViewById(R.id.view_pager);

        // init
        setIndicatorSelected(SELECTED_ITEM_INDEX);
        mViewPager.setCurrentItem(SELECTED_ITEM_INDEX);
    }

    @Override
    public void initEvents() {
        mCommunityView.setOnClickListener(this);
        mReadView.setOnClickListener(this);

        mPageAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), getFragmentList());
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                setIndicatorSelected(arg0);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_community:
                mViewPager.setCurrentItem(0);
                setIndicatorSelected(0);
                break;
            case R.id.title_read:
                mViewPager.setCurrentItem(1);
                setIndicatorSelected(1);
                break;
        }
    }

    private void setIndicatorSelected(int position) {
        if (0 == position) {
            mCommunityView.setSelected(true);
            mReadView.setSelected(false);
        } else {
            mCommunityView.setSelected(false);
            mReadView.setSelected(true);
        }
    }

    private ArrayList<Fragment> getFragmentList() {
        ArrayList<Fragment> list = new ArrayList<Fragment>();
        list.add(new CommunityFragment());
        list.add(new ReadFragment());
        return list;
    }

//    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        @Override
//        public void onPageSelected(int arg0) {
//            setIndicatorSelected(arg0);
//        }
//    };
}
