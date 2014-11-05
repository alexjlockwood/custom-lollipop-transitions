package com.alexjlockwood.transitions.textsize;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.Property;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextSizeTransition extends Transition {
    private static final String PROPNAME_TEXT_SIZE = "text_size_transition:size";

    private static final String[] TRANSITION_PROPERTIES = { PROPNAME_TEXT_SIZE };

    private static final Property<TextView, Float> TEXT_SIZE =
            new Property<TextView, Float>(Float.class, "textSize") {
                @Override
                public Float get(TextView textView) {
                    return textView.getTextSize();
                }

                @Override
                public void set(TextView textView, Float textSizePixels) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePixels);
                }
            };

    public TextSizeTransition() {
    }

    public TextSizeTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String[] getTransitionProperties() {
        return TRANSITION_PROPERTIES;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        final View view = transitionValues.view;
        if (view instanceof TextView) {
            final TextView textView = (TextView) view;
            transitionValues.values.put(PROPNAME_TEXT_SIZE, textView.getTextSize());
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Float startSize = (Float) startValues.values.get(PROPNAME_TEXT_SIZE);
        Float endSize = (Float) endValues.values.get(PROPNAME_TEXT_SIZE);

        if (startSize == null || endSize == null ||
                startSize.floatValue() == endSize.floatValue()) {
            return null;
        }

        TextView view = (TextView) endValues.view;
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, startSize);
        return ObjectAnimator.ofFloat(view, TEXT_SIZE, startSize, endSize);
    }
}
