package net.smiguel.app.view.activity;

import android.arch.lifecycle.LifecycleObserver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import net.smiguel.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public abstract class BaseActivity extends AppCompatActivity {

    Unbinder mUnbinder;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    List<LifecycleObserver> mLifecycleObservers;

    public void showLogoutConfirmationMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_confirmation_message)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_yes, (dialog, which) -> {
                    dialog.dismiss();
                    logout();
                })
                .setNegativeButton(R.string.btn_no, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void logout() {
        Timber.d("Logout user...");
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        removeObservers();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    public void addObserver(LifecycleObserver... observers) {
        if (getLifecycle() != null && observers != null) {
            if (mLifecycleObservers == null) {
                mLifecycleObservers = new ArrayList<>();
            }
            for (LifecycleObserver observer : observers) {
                getLifecycle().addObserver(observer);
                mLifecycleObservers.add(observer);
            }
        }
    }

    public void removeObservers() {
        if (getLifecycle() != null
                && mLifecycleObservers != null
                && mLifecycleObservers.size() > 0) {
            for (LifecycleObserver observer : mLifecycleObservers) {
                getLifecycle().removeObserver(observer);
            }
        }
    }
}
