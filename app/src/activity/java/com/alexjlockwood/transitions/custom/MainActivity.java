package com.alexjlockwood.transitions.custom;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private View mSharedElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_scene);
        findViewById(R.id.container).setOnClickListener(this);
        mSharedElement = findViewById(R.id.hello_world);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, ChildActivity.class), ActivityOptions.makeSceneTransitionAnimation(
                this, mSharedElement, mSharedElement.getTransitionName()).toBundle());
    }

    public static class ChildActivity extends Activity implements View.OnClickListener {
        private static final String TAG = "ChildActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.end_scene);
            findViewById(R.id.container).setOnClickListener(this);
            getWindow().setEnterTransition(TransitionUtils.makeEnterTransition());
            getWindow().setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition(this));
            setEnterSharedElementCallback(new EnterSharedElementCallback(this));
        }

        @Override
        public void onClick(View v) {
            finishAfterTransition();
        }
    }
}
