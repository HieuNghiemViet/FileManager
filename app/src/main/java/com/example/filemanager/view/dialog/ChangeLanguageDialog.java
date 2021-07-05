package com.example.filemanager.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.filemanager.R;

import java.util.ArrayList;

public class ChangeLanguageDialog extends Dialog implements View.OnClickListener {
    private Context mContext;


    public ChangeLanguageDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_change_language);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        getWindow().setAttributes(wlp);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(true);
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}

