package net.smiguel.app.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.smiguel.app.BuildConfig;
import net.smiguel.app.R;
import net.smiguel.app.constants.AppConstants;
import net.smiguel.app.util.DataUtils;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class LoginActivity extends BaseActivity {

    public static final String BUNDLE_USER_NAME = "USER_NAME";
    public static final String BUNDLE_USER_LOGIN = "USER_LOGIN";

    @BindView(R.id.txt_login)
    EditText txtLogin;

    @BindView(R.id.txt_password)
    EditText txtPassword;

    @BindView(R.id.txt_version_name)
    TextView txtVersionName;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mUnbinder = ButterKnife.bind(this);
        setViewEvents();
        txtVersionName.setText(BuildConfig.VERSION_NAME);
    }

    private void setViewEvents() {
        btnLogin.setOnClickListener(v -> {
            if (isFormValid()) {
                fakeAuthentication();
            }
        });
    }

    private void fakeAuthentication() {
        //Get fake token
        String fakeToken = String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND));
        //Persist it into local storage
        DataUtils.setSharedPreferencesValue(sharedPreferences, AppConstants.SharedData.SHARED_PREF_TOKEN, fakeToken);
        //Sends user to main page
        Intent intent = new Intent(this, MainActivity.class);
        //Set FAKE user information (Just to illustrate login process)
        intent.putExtra(BUNDLE_USER_NAME, "Sergio Miguel");
        intent.putExtra(BUNDLE_USER_LOGIN, txtLogin.getText());

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private boolean isFormValid() {
        //IMPORTANT: This need to be adjusted according application requirements
        if (TextUtils.isEmpty(txtLogin.getText().toString())) {
            txtLogin.setError(getString(R.string.error_required_field));
            txtLogin.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(txtPassword.getText().toString())) {
            txtPassword.setError(getString(R.string.error_required_field));
            txtPassword.requestFocus();
            return false;
        }
        return true;
    }
}
