package com.alexjlockwood.transitions.custom;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            View sharedElement = findViewById(R.id.hello_world);
            startActivity(new Intent(this, ChildActivity.class), ActivityOptions.makeSceneTransitionAnimation(
                    this, sharedElement, sharedElement.getTransitionName()).toBundle());
            return true;
        }
        return super.onTouchEvent(event);
    }
}
