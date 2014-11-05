package com.alexjlockwood.transitions.textsize;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.List;

public class ChildActivity extends Activity {

    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            float textSize = getResources().getDimensionPixelSize(R.dimen.small_text_size);
            TextView textView = getTextView(sharedElements);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        @Override
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            TextView textView = getTextView(sharedElements);
            float textSize = getResources().getDimensionPixelSize(R.dimen.large_text_size);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//            int widthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, 0);
//            int heightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, 0);
//            textView.measure(widthSpec, heightSpec);
//            textView.layout(textView.getLeft(), textView.getTop(), textView.getLeft() + textView.getMeasuredWidth(), textView.getBottom() + textView.getMeasuredHeight());
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
