package com.example.filemanager.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class AlphaAnimation {

    public static void visibleAnimation(final View target, long time) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.ALPHA, 0, 1));
        baseCreative.setDuration(time);
        baseCreative.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                target.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        baseCreative.startAnimationTogether();
    }

    public static void goneAnimation(final View target, long time) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.ALPHA, 1f, 0f));
        baseCreative.setDuration(time);
        baseCreative.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        baseCreative.startAnimationTogether();
    }

    public static void goneAnimation(final View target, long time, Animator.AnimatorListener listener) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.ALPHA, 1, 0));
        baseCreative.setDuration(time);
        baseCreative.addListener(listener);
        baseCreative.startAnimationTogether();
    }


    public static void visibleAnimation(final View target, long time, Animator.AnimatorListener listener) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.ALPHA, 0f, 1f));
        baseCreative.setDuration(time);
        baseCreative.addListener(listener);
        baseCreative.startAnimationTogether();
    }
}
