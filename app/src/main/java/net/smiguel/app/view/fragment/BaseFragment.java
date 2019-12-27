package net.smiguel.app.view.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.smiguel.app.R;
import net.smiguel.app.view.activity.BaseDrawerActivity;
import net.smiguel.app.view.handler.FragmentHandlerOperation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public abstract class BaseFragment extends Fragment implements FragmentHandlerOperation {

    private String mTitle;

    Unbinder mUnbinder;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTitle());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    public void showMessage(String message) {
        showMessage(message, null);
    }

    public void showMessage(String message, Throwable throwable) {
        if (message != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            if (throwable != null) {
                Timber.e(message);
                Timber.e(throwable);
            } else {
                Timber.d(message);
            }
        }
    }

    public void showAlertMessage(String title, String message) {
        showAlertMessage(title, message, null);
    }

    public void showAlertMessage(String title, String message, View.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.btn_ok, (dialogInterface, i) -> {
                    if (clickListener != null) {
                        clickListener.onClick(getView());
                    }
                    dialogInterface.dismiss();
                })
                .create()
                .show();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    //region Fragment Management
    @Override
    public void addFragment(BaseFragment fragment) {
        ((BaseDrawerActivity) getActivity()).addFragment(fragment);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack) {
        ((BaseDrawerActivity) getActivity()).addFragment(fragment, addToBackStack);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack, boolean validateCurrent) {
        ((BaseDrawerActivity) getActivity()).addFragment(fragment, addToBackStack, validateCurrent);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack, boolean validateCurrent, boolean clearBackStack) {
        ((BaseDrawerActivity) getActivity()).addFragment(fragment, addToBackStack, validateCurrent, clearBackStack);
    }

    @Override
    public void popUp() {
        ((BaseDrawerActivity) getActivity()).popUp();
    }

    @Override
    public void popUp(String name) {
        ((BaseDrawerActivity) getActivity()).popUp(name);
    }

    @Override
    public void popUpAll() {
        ((BaseDrawerActivity) getActivity()).popUpAll();
    }
    //endregion

    //region Observers
    public void addObserver(LifecycleObserver... observers) {
        ((BaseDrawerActivity) getActivity()).addObserver(observers);
    }
    //endregion
}
