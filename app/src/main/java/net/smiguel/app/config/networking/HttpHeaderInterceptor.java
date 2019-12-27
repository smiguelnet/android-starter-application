package net.smiguel.app.config.networking;

import android.content.SharedPreferences;

import net.smiguel.app.constants.AppConstants;
import net.smiguel.app.util.DataUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class HttpHeaderInterceptor implements Interceptor {
    private SharedPreferences mSharedPreferences;

    public HttpHeaderInterceptor(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String token = DataUtils.getSharedPreferencesValue(mSharedPreferences, AppConstants.SharedData.SHARED_PREF_TOKEN);

        if (token != null) {
            Timber.d("Add authorization header to request. Token: " + token);
            request = request.newBuilder().addHeader("Authorization", String.format("Bearer %s", token)).build();
        }
        return chain.proceed(request);
    }
}
