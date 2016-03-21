package com.product.helpshopping.ui.activity;

import android.os.Bundle;

import com.product.helpshopping.common.Constants;
import com.product.helpshopping.ui.base.BaseWebActivity;

/**
 * Created by Administrator on 2015/11/26 0026.
 */
public class WebViewActivity extends BaseWebActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = (String) getIntent().getSerializableExtra(Constants.SPKey.BUNDLE_URL);
        loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
