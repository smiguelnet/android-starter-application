package net.smiguel.app.view.activity;

import android.os.Bundle;
import android.util.Log;

import net.smiguel.app.R;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder = ButterKnife.bind(this);

        selectOptMenuHome();

        //Set FAKE user information (Just to illustrate login process)
        if (getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().size() == 2) {
            String userName = getIntent().getExtras().get(LoginActivity.BUNDLE_USER_NAME).toString();
            String userLogin = getIntent().getExtras().get(LoginActivity.BUNDLE_USER_LOGIN).toString();

            setDrawerUserData(userName, userLogin);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logStateTransition("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logStateTransition("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeObservers();
        logStateTransition("onDestroy");
    }

    private void logStateTransition(String state) {
        Log.d("SMIG", "state has been logged: " + state);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
