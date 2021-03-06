package com.product.helpshopping.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.product.common.utils.LogUtils;
import com.product.helpshopping.R;
import com.product.helpshopping.common.Constants;
import com.product.helpshopping.common.interfaces.ICycle;
import com.product.helpshopping.module.net.response.AlbumItem;
import com.product.helpshopping.ui.activity.WebViewActivity;
import com.product.helpshopping.ui.adapter.BannerPagerAdapter;
import com.product.helpshopping.ui.base.BaseActivity;
import com.product.helpshopping.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/25 0025.
 */
public class AutoScrollBanner extends RelativeLayout implements ICycle {
    private static final String TAG = AutoScrollBanner.class.getSimpleName();
    private static final long TIME_INTERVAL = 3000;
    private static final int PAGE_OFFSET = 10000;
    private int mCount;
    private int mRealCount;

    private Context mContext;
    // private TextView mTips;
    private LinearLayout mIndicator;
    private ScrollViewPager mViewPager;
    private List<View> mViews;

    private BannerPagerAdapter mBannerAdapter;
    private ArrayList<AlbumItem> mList;

    private Handler mHandler;
    private Runnable mRunnable;

    public AutoScrollBanner(Context context) {
        super(context);
        init();
    }

    public AutoScrollBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoScrollBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    @Override
    public void refresh(Object obj) {
        ArrayList<AlbumItem> list = (ArrayList<AlbumItem>) obj;
        if (null != list && list.size() > 0) {
            updateUI(getFakeAlbumList(list), list.size());
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {
        stopBanner();
    }

    @Override
    public void init() {
        initView(getContext());
    }

    private void initView(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.auto_scroll_banner, this);

        mContext = ctx;
        // mTips = (TextView) findViewById(R.id.txt_title);
        mIndicator = (LinearLayout) findViewById(R.id.ll_dot_group);
        mViewPager = (ScrollViewPager) findViewById(R.id.view_pager);
        mViewPager.setOnPageChangeListener(new PageChangeListener());

        mViews = new ArrayList<>();
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mViewPager.getScrollAble()) {
                    int curr = mViewPager.getCurrentItem();
                    mViewPager.setCurrentItem(curr + 1);
                    mHandler.postDelayed(this, TIME_INTERVAL);
                }
            }
        };
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int pageIndex) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int pageIndex) {
            setIndicatorEnabled(pageIndex % mRealCount);
        }
    }

    @NonNull
    private ArrayList<AlbumItem> getFakeAlbumList(ArrayList<AlbumItem> list) {
        ArrayList<AlbumItem> fakeList = new ArrayList<>();
        // 起码保证有4个item
        if (1 == list.size()) {
            fakeList.add(list.get(0));
            fakeList.add(list.get(0));
            fakeList.add(list.get(0));
            fakeList.add(list.get(0));
        } else if (2 == list.size()) {
            fakeList.add(list.get(0));
            fakeList.add(list.get(1));
            fakeList.add(list.get(0));
            fakeList.add(list.get(1));
        } else if (3 == list.size()) {
            fakeList.add(list.get(0));
            fakeList.add(list.get(1));
            fakeList.add(list.get(2));
            fakeList.add(list.get(0));
            fakeList.add(list.get(1));
            fakeList.add(list.get(2));
        } else {
            fakeList = list;
        }
        return fakeList;
    }

    private void updateUI(ArrayList<AlbumItem> list, int realCount) {
        if (null == mList) {
            mList = list;
        } else {
            if (mList.size() == list.size()) {
                return;
            } else {
                mList = list;
            }
        }
        mRealCount = realCount;
        mCount = mList.size();

        initIndicator(mRealCount);
        initBanner(mList);

        mBannerAdapter = new BannerPagerAdapter(getContext(), mViews);
        mViewPager.setAdapter(mBannerAdapter);
        if (1 == mRealCount) {
            mViewPager.setScrollAble(false);
        }
        // int curr = ((Integer.MAX_VALUE / mCount) >> 1) * mCount;
        // LogUtils.i(TAG, "Integer.MAX_VALUE = " + Integer.MAX_VALUE + " ;curr = " + curr);
        mViewPager.setCurrentItem(mCount * PAGE_OFFSET);
        mBannerAdapter.notifyDataSetChanged();

        startBanner();
    }

    private void startBanner() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mRunnable, TIME_INTERVAL);
    }

    private void stopBanner() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 初始化广告页的内容
     *
     * @param list
     */
    private void initBanner(ArrayList<AlbumItem> list) {
        // 初始化引导页
        View view = null;
        mViews.clear();
        for (AlbumItem info : list) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_banner, null);
            // TextView title = (TextView) view.findViewById(R.id.txt_title);
            AdImageView image = (AdImageView) view.findViewById(R.id.nt_image);
            image.setTag(info);
            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlbumItem info = (AlbumItem) view.getTag();
                    LogUtils.i(TAG, "onClick info = " + info);

                    if (null != info) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.SPKey.BUNDLE_URL, info.getUrl());
                        ((BaseActivity) getContext()).openActivity(WebViewActivity.class, bundle);
                    }
                }
            });

            // title.setText(info.getTitle());
            CommonUtils.loadImage(image, info.getCover());
            mViews.add(view);
        }
    }

    /**
     * 初始化广告栏下面的指示点
     *
     * @param count
     */
    private void initIndicator(int count) {
        // 初始化引导点indicator
        LinearLayout.LayoutParams params;
        for (int i = 0; i < count; i++) {
            View dot = new View(getContext());
            dot.setBackgroundResource(R.drawable.selector_guide_indicator);
            params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 5;
            params.rightMargin = 5;
            dot.setLayoutParams(params);
            dot.setEnabled(false);
            mIndicator.addView(dot);
        }

        if (mIndicator.getChildCount() != 0)
            mIndicator.getChildAt(0).setEnabled(true);
    }

    private void setIndicatorEnabled(int pos) {
        for (int i = 0; i < mIndicator.getChildCount(); i++) {
            mIndicator.getChildAt(i).setEnabled(false);
        }
        mIndicator.getChildAt(pos).setEnabled(true);
    }
}
