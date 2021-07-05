package com.example.filemanager.view.custom;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.filemanager.R;
import com.example.filemanager.animation.MoveAnimation;
import com.example.filemanager.util.ScreenUtils;

public class SettingView extends RelativeLayout {
    private Context mContext;

    public SettingView(Context context) {
        super(context);
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.setting_view, this);
    }

    private void initData() {

    }

    public boolean isOpening() {
        return getVisibility() == VISIBLE;
    }

    public void openView() {
        if (getVisibility() == GONE) {
            MoveAnimation.openViewFromRight(this, ScreenUtils.getScreenWidth(mContext), 300, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    initData();
                }


                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }


    public void closeView() {

        MoveAnimation.closeViewToRight(this, ScreenUtils.getScreenWidth(mContext), 300, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
