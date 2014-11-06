package com.alexjlockwood.transitions.custom;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.List;

public class ChildActivity extends Activity {
    private static final String TAG = "ChildActivity";

    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            TextView textView = (TextView) sharedElements.get(0);
            float textSize = getResources().getDimensionPixelSize(R.dimen.small_text_size);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textView.setTextColor(getResources().getColor(R.color.blue));
        }

        @Override
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            TextView textView = (TextView) sharedElements.get(0);
            int oldMeasuredWidth = textView.getMeasuredWidth();
            int oldMeasuredHeight = textView.getMeasuredHeight();
            float textSize = getResources().getDimensionPixelSize(R.dimen.large_text_size);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            textView.measure(widthSpec, heightSpec);
            int newMeasuredWidth = textView.getMeasuredWidth();
            int newMeasuredHeight = textView.getMeasuredHeight();
            int measuredWidthDiff = newMeasuredWidth - oldMeasuredWidth;
            int measuredHeightDiff = newMeasuredHeight - oldMeasuredHeight;
            textView.layout(textView.getLeft() - measuredWidthDiff / 2, textView.getTop() - measuredHeightDiff / 2,
                    textView.getRight() + measuredWidthDiff / 2, textView.getBottom() + measuredHeightDiff / 2);
            textView.setTextColor(getResources().getColor(R.color.light_blue));
        }
    };

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        setEnterSharedElementCallback(mCallback);
        postponeEnterTransition();

        mText = (TextView) findViewById(R.id.hello_world);
        mText.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mText.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        getWindow().setEnterTransition(fade);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            finishAfterTransition();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
