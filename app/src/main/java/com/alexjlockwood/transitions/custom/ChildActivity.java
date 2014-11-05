package com.alexjlockwood.transitions.custom;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.List;

public class ChildActivity extends Activity {

    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//            float textSize = getResources().getDimensionPixelSize(R.dimen.small_text_size);
//            TextView textView = getTextView(sharedElements);
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            TextView textView = getTextView(sharedElements);
            textView.setTextColor(getResources().getColor(R.color.red));
        }

        @Override
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//            TextView textView = getTextView(sharedElements);
//            float textSize = getResources().getDimensionPixelSize(R.dimen.large_text_size);
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//            int widthSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, 0);
//            int heightSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, 0);
//            textView.measure(widthSpec, heightSpec);
//            textView.layout(textView.getLeft(), textView.getTop(), textView.getLeft() + textView.getMeasuredWidth(), textView.getBottom() + textView.getMeasuredHeight());
            TextView textView = getTextView(sharedElements);
            textView.setTextColor(getResources().getColor(R.color.green));
        }

        private TextView getTextView(List<View> sharedElements) {
            for (View view : sharedElements) {
                if (view instanceof TextView) {
                    return (TextView) view;
                }
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        setEnterSharedElementCallback(mCallback);
        postponeEnterTransition();
        getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

        View actionBar = getWindow().getDecorView().findViewById(getResources().getIdentifier(
                "action_bar_container", "id", "android"));

        Transition fadeIn = new BackgroundFade(Color.RED, Color.GREEN);
        fadeIn.addTarget(actionBar);
        Transition fadeOut = new BackgroundFade(Color.GREEN, Color.RED);
        fadeOut.addTarget(actionBar);
        getWindow().setExitTransition(fadeOut);
        getWindow().setEnterTransition(fadeIn);
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
