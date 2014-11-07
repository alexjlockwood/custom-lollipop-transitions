package com.alexjlockwood.transitions.custom;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content, new MainFragment()).commit();
        }
    }

    public static class MainFragment extends Fragment implements View.OnClickListener {
        private static final String TAG = "MainFragment";

        private View mSharedElement;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.start_scene, container, false);
            view.setOnClickListener(this);
            mSharedElement = view.findViewById(R.id.hello_world);
            return view;
        }

        @Override
        public void onClick(View v) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(android.R.id.content, ChildFragment.newInstance(getActivity()));
            ft.addSharedElement(mSharedElement, mSharedElement.getTransitionName());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public static class ChildFragment extends Fragment implements View.OnClickListener {
        private static final String TAG = "ChildFragment";

        public static ChildFragment newInstance(Context context) {
            ChildFragment frag = new ChildFragment();
            frag.setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition(context));
            frag.setEnterSharedElementCallback(new EnterSharedElementCallback(context));
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.end_scene, container, false);
            view.setOnClickListener(this);
            return view;
        }

        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStackImmediate();
        }
    }
}
