package net.smiguel.app.view.handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;

import net.smiguel.app.R;
import net.smiguel.app.view.fragment.BaseFragment;

import timber.log.Timber;

public class FragmentHandler implements FragmentHandlerOperation {

    private final FragmentManager mFragmentManager;

    public FragmentHandler(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public void addFragment(BaseFragment fragment) {
        addFragment(fragment, true, false);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack) {
        addFragment(fragment, addToBackStack, false);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack, boolean validateCurrent) {
        addFragment(fragment, addToBackStack, validateCurrent, false);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack, boolean validateCurrent, boolean clearBackStack) {
        BaseFragment currentFragment = getCurrentFragment();

        if (validateCurrent
                && currentFragment != null
                && currentFragment.getClass().equals(fragment.getClass())) {
            Timber.d("The fragment received by addFragment function is already been shown");
            return;
        }
        String title = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.app_frame_layout, fragment, title);

        if (clearBackStack) {
            popUpAll();
        }

        //Animation (current fragment)
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        accelerateDecelerateInterpolator.getInterpolation(1.2f);

        if (currentFragment != null) {
            Slide slideOut = new Slide();
            slideOut.setSlideEdge(Gravity.LEFT);
            slideOut.setDuration(15);
            slideOut.setInterpolator(accelerateDecelerateInterpolator);
            currentFragment.setExitTransition(slideOut);
        }

        //Animation (new fragment)
        Slide slideIn = new Slide();
        slideIn.setSlideEdge(Gravity.RIGHT);
        slideIn.setStartDelay(25);
        slideIn.setDuration(50);
        slideIn.setInterpolator(accelerateDecelerateInterpolator);
        fragment.setEnterTransition(slideIn);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(title);
        }
        fragmentTransaction.commit();

        //Trace backstack items
        debugBackStack();
    }

    @Override
    public void popUp() {
        mFragmentManager.popBackStackImmediate();
    }

    @Override
    public void popUp(String name) {
        mFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void popUpAll() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public BaseFragment getCurrentFragment() {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        FragmentManager.BackStackEntry currentStackEntry = mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1);
        Fragment fragment = mFragmentManager.findFragmentByTag(currentStackEntry.getName());
        if (fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        } else {
            return null;
        }
    }

    public void debugBackStack() {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            Timber.d("There is no item on back stack");
            return;
        }
        Timber.d("There are %d items on back stack", mFragmentManager.getBackStackEntryCount());

        for (int index = 0; index < mFragmentManager.getBackStackEntryCount(); index++) {
            FragmentManager.BackStackEntry backStackEntryAt = mFragmentManager.getBackStackEntryAt(index);
            Timber.d("-------> %d", index);
            Timber.d("-------> %d ID: %s", index, backStackEntryAt.getId());
            Timber.d("-------> %d NAME: %s", index, backStackEntryAt.getName());
            Timber.d(" ");
        }
    }
}
