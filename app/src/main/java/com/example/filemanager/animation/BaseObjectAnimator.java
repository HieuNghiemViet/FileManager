package com.example.filemanager.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.view.View;
import android.view.animation.Interpolator;

import java.util.ArrayList;

public class BaseObjectAnimator {
    private static View view;
    private ObjectAnimator objectAnimator;
    private ArrayList<ObjectAnimator> objectAnimators = new ArrayList<>();


    public static String TRANSLATION_Y = "translationY";
    public static String TRANSLATION_X = "translationX";
    public static String ALPHA = "alpha";
    public static String ROTATION = "rotation";
    public static String ROTATION_Y = "rotationY";
    public static String ROTATION_X = "rotationX";
    public static String SCALE_X = "scaleX";
    public static String SCALE_Y = "scaleY";
    public static String X = "x";
    public static String Y = "y";
    public static String PIVOT_X = "pivotX";
    public static String PIVOT_Y = "pivotY";


    public static String BACKGROUND_COLOR = "backgroundColor";
    public static String CLIP_BOUNDS = "clipBounds";

    public BaseObjectAnimator(View view) {
        objectAnimator = new ObjectAnimator();
        this.view = view;
    }

    public BaseObjectAnimator() {
        objectAnimator = new ObjectAnimator();
    }

    public void setAnimator(View view, String type, float... listPoint) {
        objectAnimator = ObjectAnimator.ofFloat(view, type, listPoint);
    }

    public void setAnimator(String type, float... listPoint) {
        objectAnimator = ObjectAnimator.ofFloat(view, type, listPoint);
    }

    public void setItemAnimator(View view, String type, float... listPoint) {
        objectAnimator = ObjectAnimator.ofFloat(view, type, listPoint);
        objectAnimators.add(objectAnimator);
    }

    public void setItemAnimator(String type, float... listPoint) {
        objectAnimator = ObjectAnimator.ofFloat(view, type, listPoint);
        objectAnimators.add(objectAnimator);
    }

    public ObjectAnimator getObjectAnimator() {
        return objectAnimator;
    }

    public ArrayList<ObjectAnimator> getListObjectAnimator() {
        return objectAnimators;
    }


    public void setInterpolator(Interpolator interpolator) {
        objectAnimator.setInterpolator(interpolator);
    }

    public static ObjectAnimator makeAnimator(View view, String type, float... listPoint) {
        return ObjectAnimator.ofFloat(view, type, listPoint);
    }

    public static ObjectAnimator makeAnimator(String type, float... listPoint) {
        return ObjectAnimator.ofFloat(view, type, listPoint);
    }

    public void setDuration(long time) {
        objectAnimator.setDuration(time);
    }

    public void setRepeatCount(int times) {
        objectAnimator.setRepeatCount(times);
    }

    public void setRepeatMode(int value) {
        objectAnimator.setRepeatMode(value);
    }

    public void addListener(Animator.AnimatorListener animatorListener) {
        objectAnimator.addListener(animatorListener);
    }

    public void cancelAnim() {
        objectAnimator.cancel();
    }

    public static ObjectAnimator makeAnimatorEvaluator(View view, String type, TypeEvaluator typeEvaluator) {
        return ObjectAnimator.ofObject(view, type, typeEvaluator);
    }

    public static ObjectAnimator makeAnimatorValueHolder(View view, PropertyValuesHolder... values) {
        return ObjectAnimator.ofPropertyValuesHolder(view, values);
    }
}
