package net.smiguel.app.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.smiguel.app.R;
import net.smiguel.app.view.activity.OnBoardingActivity;
import net.smiguel.app.view.fragment.OnBoardingFragment;

public class OnBoardingPageAdapter extends FragmentStatePagerAdapter {

    public OnBoardingPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        OnBoardingFragment onBoardingFragment = null;
        switch (position) {
            case 0:
                onBoardingFragment = OnBoardingFragment.newInstance(R.layout.fragment_onboarding_step1);
                break;
            case 1:
                onBoardingFragment = OnBoardingFragment.newInstance(R.layout.fragment_onboarding_step2);
                break;
            case 2:
                onBoardingFragment = OnBoardingFragment.newInstance(R.layout.fragment_onboarding_step3);
                break;
        }
        return onBoardingFragment;
    }

    @Override
    public int getCount() {
        return OnBoardingActivity.STEPS;
    }
}
