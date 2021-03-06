package com.product.helpshopping.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.product.helpshopping.R;

/**
 * 加载对话框
 *
 * @author Peng Yi
 */
public class LoadingDialog extends Dialog {
    // private CharSequence mText = null;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(Context context, CharSequence text) {
        this(context, R.style.FullScreenDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init(text);
    }

    private void init(CharSequence text) {
        setContentView(R.layout.layout_dialog_loading);
        final TextView msgText = (TextView) findViewById(R.id.txt_tips);
        if (TextUtils.isEmpty(text)) {
            msgText.setVisibility(View.GONE);
        } else {
            msgText.setText(text);
        }
    }
}
