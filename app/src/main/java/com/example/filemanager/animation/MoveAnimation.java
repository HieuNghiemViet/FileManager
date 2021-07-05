package com.example.filemanager.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MoveAnimation {
    public static void openViewFromBottom(final View target, int distance, int duration, Animator.AnimatorListener animatorListener) {
        BaseCreative baseCreative = new BaseCreative();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_Y, distance, 0);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        baseCreative.addAnimator(objectAnimator);
        // baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.ALPHA, 0, 1));
        baseCreative.setDuration(duration);
        baseCreative.addListener(animatorListener);
        baseCreative.startAnimationTogether();
    }

    public static void closeViewToBottom(final View target, int distance, int duration, Animator.AnimatorListener listener) {
        BaseCreative baseCreative = new BaseCreative();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_Y, 0, distance);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        baseCreative.addAnimator(objectAnimator);
        baseCreative.setDuration(duration);
        baseCreative.addListener(listener);
        baseCreative.startAnimationTogether();
    }

    public static void openViewFromBottom(final View target, int distance, int duration) {
        BaseCreative baseCreative = new BaseCreative();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_Y, distance, 0);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        baseCreative.addAnimator(objectAnimator);
        baseCreative.setDuration(duration);
        baseCreative.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setVisibility(View.VISIBLE);
            }
        });
        baseCreative.startAnimationTogether();
    }

    public static void closeViewToBottom(final View target, int distance, int duration) {
        BaseCreative baseCreative = new BaseCreative();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_Y, 0, distance);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        baseCreative.addAnimator(objectAnimator);
        baseCreative.setDuration(duration);
        baseCreative.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                target.setVisibility(View.GONE);
            }
        });
        baseCreative.startAnimationTogether();
    }

    public static void openViewFromRight(View target, int distance, long time, Animator.AnimatorListener listener) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_X, distance, 0));
        baseCreative.setDuration(time);
        baseCreative.addListener(listener);
        baseCreative.startAnimationTogether();
    }

    public static void closeViewToRight(View target, int distance, long time, Animator.AnimatorListener listener) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_X, 0, distance));
        baseCreative.setDuration(time);
        baseCreative.addListener(listener);
        baseCreative.startAnimationTogether();
    }

    public static void openViewFromRight(View target, int distance, long time) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_X, distance, 0));
        baseCreative.setDuration(time);
        baseCreative.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setVisibility(View.VISIBLE);
            }
        });
        baseCreative.startAnimationTogether();
    }

    public static void closeViewToRight(final View target, int distance, long time) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_X, 0, distance));
        baseCreative.setDuration(time);
        baseCreative.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                target.setVisibility(View.GONE);
            }
        });
        baseCreative.startAnimationTogether();
    }


    public static void openViewFromLeft(View target, int distance, long time, Animator.AnimatorListener listener) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_X, -distance, 0));
        baseCreative.setDuration(time);
        baseCreative.addListener(listener);
        baseCreative.startAnimationTogether();
    }

    public static void closeViewToLeft(View target, int distance, long time, Animator.AnimatorListener listener) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_X, 0, -distance));
        baseCreative.setDuration(time);
        baseCreative.addListener(listener);
        baseCreative.startAnimationTogether();
    }

    public static void openViewFromLeft(View target, int distance, long time) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_X, -distance, 0));
        baseCreative.setDuration(time);
        baseCreative.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setVisibility(View.VISIBLE);
            }
        });
        baseCreative.startAnimationTogether();
    }

    public static void closeViewToLeft(View target, int distance, long time) {
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_X, 0, -distance));
        baseCreative.setDuration(time);
        baseCreative.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                target.setVisibility(View.GONE);
            }
        });
        baseCreative.startAnimationTogether();
    }


    public static void openViewFromTop(final View target, int distance, int duration) {
        BaseCreative baseCreative = new BaseCreative();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_Y, -distance, 0);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        baseCreative.addAnimator(objectAnimator);
        baseCreative.setDuration(duration);
        baseCreative.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setVisibility(View.VISIBLE);
            }
        });
        baseCreative.startAnimationTogether();
    }

    public static void closeViewFromTop(final View target, int distance, int duration) {
        BaseCreative baseCreative = new BaseCreative();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, BaseObjectAnimator.TRANSLATION_Y, 0, -distance);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        baseCreative.addAnimator(objectAnimator);
        baseCreative.setDuration(duration);
        baseCreative.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                target.setVisibility(View.GONE);
            }
        });
        baseCreative.startAnimationTogether();
    }
}
