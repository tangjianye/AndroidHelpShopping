package com.product.helpshopping.ui.activity;

import android.os.Bundle;
import android.view.ViewStub;

import com.product.common.manager.AppManager;
import com.product.common.utils.SPUtils;
import com.product.helpshopping.R;
import com.product.helpshopping.common.Constants;
import com.product.helpshopping.ui.base.AppBaseActivity;
import com.product.helpshopping.ui.layer.GuideLayer;
import com.product.helpshopping.ui.layer.SplashLayer;


public class SplashActivity extends AppBaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private SplashLayer mSplashLayer = null;
    private GuideLayer mGuideLayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 防止用户点击状态栏重新激活app
        boolean isAppLive = AppManager.getInstance().resumeApp(this);
        super.onCreate(savedInstanceState);
        if (isAppLive) {
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);
        switchView(getGuideSwitch());
    }

    private Boolean getGuideSwitch() {
        return (Boolean) SPUtils.get(this, Constants.SPKey.GUIDE_KEY, false);
    }

    private void switchView(Boolean havedShowGuide) {
        if (havedShowGuide) {
            ViewStub stub = (ViewStub) findViewById(R.id.vs_welcome);
            stub.inflate();
            mSplashLayer = (SplashLayer) findViewById(R.id.in_welcome);
            mSplashLayer.refresh(null);
        } else {
            ViewStub stub = (ViewStub) findViewById(R.id.vs_guide);
            stub.inflate();
            mGuideLayer = (GuideLayer) findViewById(R.id.in_guide);
            mGuideLayer.refresh(null);

            SPUtils.put(this, Constants.SPKey.GUIDE_KEY, true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mSplashLayer) {
            mSplashLayer.destroy();
        }
        if (null != mGuideLayer) {
            mGuideLayer.destroy();
        }
    }
}
