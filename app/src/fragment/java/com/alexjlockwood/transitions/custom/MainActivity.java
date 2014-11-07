package com.alexjlockwood.transitions.custom;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SharedElementCallback;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private static final String MAIN_FRAGMENT_TAG = "main_fragment_tag";
    private static final String CHILD_FRAGMENT_TAG = "child_fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content, new MainFragment(), MAIN_FRAGMENT_TAG).commit();
        }
    }

    @Override
    public void onBackPressed() {
        ChildFragment child = (ChildFragment) getFragmentManager().findFragmentByTag(CHILD_FRAGMENT_TAG);
        if (child != null) {
            child.onBackPressed();
        }
        super.onBackPressed();
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
            ChildFragment child = new ChildFragment();
            child.setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition(getActivity()));
            child.setEnterSharedElementCallback(new EnterSharedElementCallback(getActivity()));
            ft.replace(android.R.id.content, child, CHILD_FRAGMENT_TAG);
            ft.addSharedElement(mSharedElement, mSharedElement.getTransitionName());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public static class ChildFragment extends Fragment implements View.OnClickListener {
        private static final String TAG = "ChildFragment";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.end_scene, container, false);
            view.setOnClickListener(this);
            setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition(getActivity()));
            setEnterSharedElementCallback(new EnterSharedElementCallback(getActivity()));
            return view;
        }

        @Override
        public void onClick(View v) {
            onBackPressed();
            getFragmentManager().popBackStackImmediate();
        }

        public void onBackPressed() {
            // For whatever reason, the start/end methods are not called in the same order as
            // with Activity Transitions, so we need to set a new shared element callback here
            // and invert the order ourselves.
            final SharedElementCallback enterCallback = new EnterSharedElementCallback(getActivity());
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    enterCallback.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                }

                @Override
                public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    enterCallback.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                }
            });
        }
    }
}
