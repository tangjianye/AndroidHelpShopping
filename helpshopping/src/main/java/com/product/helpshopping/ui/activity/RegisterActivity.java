package com.product.helpshopping.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
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
import com.product.helpshopping.module.net.response.MaskObjectSet;
import com.product.helpshopping.module.net.response.RegisterItem;
import com.product.helpshopping.ui.base.BaseTitleActivity;
import com.product.common.utils.AnimatorUtils;
import com.product.common.utils.LogUtils;
import com.product.common.utils.SPUtils;
import com.product.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseTitleActivity implements IInit, View.OnClickListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private String mAccount;
    private String mPassword;
    private String mEmial;

    private EditText mEtAccount;
    private EditText mEtPassword;
    private EditText mEtEmail;
    private Button mBtnSubmit;
    // private TextView mTxtQuicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
        initTitles();
        initViews();
        initEvents();
    }

    @Override
    protected void setContentLayer() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void initDatas() {
    }

    @Override
    public void initTitles() {
        mTitleTips.setText(R.string.title_activity_register);
    }

    @Override
    public void initViews() {
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtEmail = (EditText) findViewById(R.id.et_email);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnSubmit.setText(R.string.action_register);

        findViewById(R.id.txt_quick_register).setVisibility(View.GONE);
    }

    @Override
    public void initEvents() {
        findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        AnimatorSet set = getAnimatorSet(mBtnSubmit);
        mAccount = mEtAccount.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        mEmial = mEtEmail.getText().toString().trim();

        if (StringUtils.isEmpty(mAccount)) {
            showToast(R.string.error_invalid_account);
            set.start();
        } else if (StringUtils.isEmpty(mPassword)) {
            showToast(R.string.error_invalid_password);
            set.start();
        } else if (StringUtils.isEmpty(mEmial)) {
            showToast(R.string.error_invalid_email);
            set.start();
        } else {
            String url = API.getUrl(API.REGISTER);
            request(url, mAccount, mPassword, mEmial);
        }
    }

    @NonNull
    private AnimatorSet getAnimatorSet(View view) {
        AnimatorSet set = AnimatorUtils.createAnimatorSet();
        ObjectAnimator translationX = AnimatorUtils.ofFloat(view, "translationX", 0.0f, 20.0f, 0.0f);
        ObjectAnimator translationY = AnimatorUtils.ofFloat(view, "translationY", 0, 10);
        set.setDuration(getResources().getInteger(R.integer.anim_shake_duration));
        set.setInterpolator(new CycleInterpolator(getResources().getInteger(R.integer.anim_shake_cycle_num)));
        set.play(translationX);
        // set.play(translationX).with(translationY);
        return set;
    }

    private void request(final String url, final String account, final String password, final String email) {
        showLoadingDialog(getString(R.string.default_loading_tips));

        GsonRequest<MaskObjectSet<RegisterItem>> request = new GsonRequest<MaskObjectSet<RegisterItem>>(
                Request.Method.POST,
                url,
                new TypeToken<MaskObjectSet<RegisterItem>>() {
                }.getType(),
                new Response.Listener<MaskObjectSet<RegisterItem>>() {
                    @Override
                    public void onResponse(MaskObjectSet<RegisterItem> response) {
                        dismissLoadingDialog();
                        if (null != response && response.getRsm() != null) {
                            showToast(response.getRsm().getUid() + "");
                            SPUtils.put(RegisterActivity.this, Constants.SPKey.ACCOUNT_UID, response.getRsm().getUid());
                            SPUtils.put(RegisterActivity.this, Constants.SPKey.ACCOUNT_NAME, account);
                            SPUtils.put(RegisterActivity.this, Constants.SPKey.ACCOUNT_PASSWORD, password);
                            finish();
                        } else {
                            showToast((null != response) ? response.getErr() : getString(R.string.default_get_data_fail));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissLoadingDialog();
                        showToast(R.string.default_get_data_fail);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_name", account);
                map.put("password", password);
                map.put("email", email);
                LogUtils.i(TAG, "map = " + map.toString());
                return map;
            }
        };

        VolleyManager.getInstance().addToRequestQueue(request, url);
    }
}
