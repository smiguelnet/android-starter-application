package net.smiguel.app.config.networking;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;

import net.smiguel.app.App;
import net.smiguel.app.BuildConfig;
import net.smiguel.app.constants.AppConstants;
import net.smiguel.app.util.DataUtils;
import net.smiguel.app.util.NetworkUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class RemoteService {

    private static volatile RemoteService INSTANCE = null;

    public static RemoteService getInstance() {
        if (INSTANCE == null) {
            synchronized (RemoteService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteService();
                }
            }
        }
        return INSTANCE;
    }

    //region Interceptors
    private ChuckInterceptor getChuckInterceptor() {
        return new ChuckInterceptor(App.getApplication());
    }

    private StethoInterceptor getStethoInterceptor() {
        return new StethoInterceptor();
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(Timber::d);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private HttpHeaderInterceptor getHttpHeaderInterceptor() {
        return new HttpHeaderInterceptor(DataUtils.getSharedPreferences(App.getApplication()));
    }
    //endregion

    //region Cache
    private Cache getCache(File applicationCacheDir) {
        int totalMbToCache = 2;
        int cacheSize = totalMbToCache * 1024 * 1024;
        return new Cache(applicationCacheDir, cacheSize);
    }
    //endregion

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            clientBuilder
                    .addInterceptor(getChuckInterceptor())
                    .addInterceptor(getHttpLoggingInterceptor())
                    .addNetworkInterceptor(getStethoInterceptor());
        }
        clientBuilder
                .addInterceptor(getHttpHeaderInterceptor());

        if (BuildConfig.APP_ENDPOINT_USE_HTTP_CACHE) {
            clientBuilder
                    .cache(getCache(App.getApplication().getCacheDir()));
        }
        clientBuilder
                .connectTimeout(AppConstants.Network.HTTP_CONN_SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(AppConstants.Network.HTTP_READ_SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(AppConstants.Network.HTTP_WRITE_SECONDS_TIMEOUT, TimeUnit.SECONDS);

        return clientBuilder.build();
    }

    public Retrofit getRetrofit() {
        return new Retrofit
                .Builder()
                .baseUrl(BuildConfig.APP_ENDPOINT_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(NetworkUtils.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
