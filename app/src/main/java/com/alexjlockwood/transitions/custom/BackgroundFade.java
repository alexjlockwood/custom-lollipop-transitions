package com.alexjlockwood.transitions.custom;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;

public class BackgroundFade extends Visibility {
    private int mStartColor;
    private int mEndColor;

    public BackgroundFade(int startColor, int endColor) {
        super();
        mStartColor = startColor;
        mEndColor = endColor;
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        ObjectAnimator animator = ObjectAnimator.ofInt(view, "backgroundColor", mStartColor, mEndColor);
        animator.setEvaluator(new ArgbEvaluator());
        return animator;
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues,
                                TransitionValues endValues) {
        ObjectAnimator animator = ObjectAnimator.ofInt(view, "backgroundColor", mEndColor, mStartColor);
        animator.setEvaluator(new ArgbEvaluator());
        return animator;
    }
}