package net.smiguel.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.smiguel.app.constants.AppConstants;

import retrofit2.HttpException;
import retrofit2.Response;

public class NetworkUtils {

    public static boolean isHttpStatusCode(Throwable throwable, int statusCode) {
        return throwable instanceof HttpException && ((HttpException) throwable).code() == statusCode;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isValidHttpBaseResponse(Response response) {
        return response != null && response.isSuccessful();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setDateFormat(AppConstants.Network.GSON_DATE_FORMAT)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

}
