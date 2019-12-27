package net.smiguel.app.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OnBoardingFragment extends BaseFragment {

    final static String LAYOUT_ID = "layout_id";

    public static OnBoardingFragment newInstance(int layoutId) {
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);

        OnBoardingFragment onBoardingFragment = new OnBoardingFragment();
        onBoardingFragment.setArguments(bundle);
        return onBoardingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
    }
}
