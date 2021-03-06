package com.product.helpshopping.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.product.helpshopping.R;
import com.product.helpshopping.common.Constants;
import com.product.helpshopping.common.interfaces.IInit;
import com.product.helpshopping.module.net.API;
import com.product.helpshopping.module.net.VolleyManager;
import com.product.helpshopping.module.net.request.GsonRequest;
import com.product.helpshopping.module.net.response.AlbumItem;
import com.product.helpshopping.module.net.response.MaskArraySet;
import com.product.helpshopping.ui.adapter.AlbumListAdapter;
import com.product.helpshopping.ui.base.BaseTitleActivity;
import com.product.helpshopping.ui.view.XListView;
import com.product.helpshopping.ui.view.XListView.IXListViewListener;
import com.product.common.utils.LogUtils;
import com.product.common.utils.TimeUtils;

import java.util.ArrayList;

public class AlbumXListActivity extends BaseTitleActivity implements IInit, AdapterView.OnItemClickListener, IXListViewListener {
    private static final String TAG = AlbumXListActivity.class.getSimpleName();
    private static final int FRIST_PAGE = 1;
    private static final boolean IS_INIT = true;

    private View mLyContentTips;
    private XListView mListView;
    private AlbumListAdapter mListAdapter;

    private int mPage = FRIST_PAGE;
    private ArrayList<AlbumItem> mAlbumDataSet;

    // 加载方式
    private enum Status {
        LordMore,
        Refresh,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDatas();
        initTitles();
        initViews();
        initEvents();

        request(IS_INIT, getUrl(FRIST_PAGE), Status.LordMore);
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
    protected void setContentLayer() {
        setContentView(R.layout.activity_xlist_album);
    }

    @Override
    public void initDatas() {
        mAlbumDataSet = new ArrayList<>();
    }

    @Override
    public void initTitles() {
        mTitleTips.setText(R.string.main_album);
    }

    private void refreshContentTips(boolean visible) {
        mLyContentTips.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initViews() {
        mLyContentTips = findViewById(R.id.ly_tips_content);
        mLyContentTips.setVisibility(View.GONE);

        mListView = (XListView) findViewById(R.id.lv_content);
        mListView.setPullLoadEnable(true);
        mListAdapter = new AlbumListAdapter(this, mAlbumDataSet);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void initEvents() {
        mListView.setOnItemClickListener(this);
        mListView.setXListViewListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlbumItem info = mAlbumDataSet.get(i);
        LogUtils.i(TAG, "onItemClick i = " + i + " ; info = " + info);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.SPKey.BUNDLE_URL, info.getUrl());
        openActivity(WebViewActivity.class, bundle);
    }

    @Override
    public void onLoadMore() {
        LogUtils.i(TAG, "onLoadMore: mPage = " + mPage);
        request(!IS_INIT, getUrl(mPage), Status.LordMore);
    }

    @Override
    public void onRefresh() {
        request(!IS_INIT, getUrl(FRIST_PAGE), Status.Refresh);
    }

    /**
     * 重新设置xlistview的状态
     */
    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(TimeUtils.getCurrentTimeInString());
    }

    private void request(final boolean isInit, final String url, final Status mode) {
        showLoadingDialog(getString(R.string.default_loading_tips));

        GsonRequest<MaskArraySet<AlbumItem>> request = new GsonRequest<MaskArraySet<AlbumItem>>(
                Request.Method.GET,
                url,
                new TypeToken<MaskArraySet<AlbumItem>>() {
                }.getType(),
                new Response.Listener<MaskArraySet<AlbumItem>>() {
                    @Override
                    public void onResponse(MaskArraySet<AlbumItem> response) {
                        dismissLoadingDialog();
                        onLoad();
                        if (null != response && response.getRsm() != null && response.getRsm().size() > 0) {
                            response(mode, response.getRsm());
                            mPage++;
                        } else {
                            showToast(R.string.default_no_more_date);
                        }
                        refreshContentTips(mAlbumDataSet.size() > 0 ? false : true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissLoadingDialog();
                        onLoad();
                        showToast(R.string.default_get_data_fail);
                    }
                }
        );
        VolleyManager.getInstance().addToRequestQueue(request, url);
    }

    @NonNull
    private String getUrl(int page) {
        return API.getCategoryUrl(API.CATEGORY_CONTENT, page);
    }

    private void response(final Status mode, final ArrayList<AlbumItem> list) {
        if (Status.Refresh == mode) {
            mAlbumDataSet.clear();
        }

        // 创建一个假广告占位数据
        AlbumItem banner = new AlbumItem();
        mAlbumDataSet.add(banner);
        for (AlbumItem item : list) {
            mAlbumDataSet.add(item);
        }
        mListAdapter.notifyDataSetChanged();
    }
}

