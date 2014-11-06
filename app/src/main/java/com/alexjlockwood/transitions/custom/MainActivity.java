package com.alexjlockwood.transitions.custom;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private View mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = findViewById(R.id.hello_world);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            List<Pair<View, String>> pairs = new ArrayList<>();
            addSharedElement(pairs, mText, mText.getTransitionName());
            Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, pairs.toArray(new Pair[pairs.size()])).toBundle();
            startActivity(new Intent(this, ChildActivity.class), options);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private static void addSharedElement(List<Pair<View, String>> pairs, View view, String transitionName) {
        pairs.add(Pair.create(view, transitionName));
    }
}
