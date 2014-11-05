package com.alexjlockwood.transitions.custom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Recolor extends ChangeBounds {

    private static final String PROPNAME_BACKGROUND = "android:recolor:background";
    private static final String PROPNAME_TEXT_COLOR = "android:recolor:textColor";

    public Recolor() {
        super();
    }

    public Recolor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_BACKGROUND, transitionValues.view.getBackground());
        if (transitionValues.view instanceof TextView) {
            transitionValues.values.put(PROPNAME_TEXT_COLOR,
                    ((TextView)transitionValues.view).getCurrentTextColor());
        }
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        Animator changeBounds = super.createAnimator(sceneRoot, startValues, endValues);
        if (changeBounds == null || startValues == null || endValues == null) {
            return null;
        }
        List<Animator> animators = new ArrayList<>();
        animators.add(changeBounds);

        final View view = endValues.view;
        Drawable startBackground = (Drawable) startValues.values.get(PROPNAME_BACKGROUND);
        Drawable endBackground = (Drawable) endValues.values.get(PROPNAME_BACKGROUND);
        boolean changed = false;
        if (startBackground instanceof ColorDrawable && endBackground instanceof ColorDrawable) {
            ColorDrawable startColor = (ColorDrawable) startBackground;
            ColorDrawable endColor = (ColorDrawable) endBackground;
            if (startColor.getColor() != endColor.getColor()) {
                endColor.setColor(startColor.getColor());
                changed = true;
                animators.add(ObjectAnimator.ofObject(endBackground, "color",
                        new ArgbEvaluator(), startColor.getColor(), endColor.getColor()));
            }
        }
        if (animators.size() == 1 && view instanceof TextView) {
            TextView textView = (TextView) view;
            int start = (Integer) startValues.values.get(PROPNAME_TEXT_COLOR);
            int end = (Integer) endValues.values.get(PROPNAME_TEXT_COLOR);
            if (start != end) {
                textView.setTextColor(end);
                changed = true;
                animators.add(ObjectAnimator.ofObject(textView, "textColor",
                        new ArgbEvaluator(), start, end));
            }
        }
        AnimatorSet anim = new AnimatorSet();
        anim.setInterpolator(getInterpolator());
        anim.playTogether(animators);
        return anim;
    }
}