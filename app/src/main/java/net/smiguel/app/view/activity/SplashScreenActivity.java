package net.smiguel.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import net.smiguel.app.R;

public class SplashScreenActivity extends BaseActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 1500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, OnBoardingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIMEOUT);
    }
}
