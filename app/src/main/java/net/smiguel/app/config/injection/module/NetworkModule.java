package net.smiguel.app.config.injection.module;

/**
 * Used when Rest communication will be performed in FOREGROUND, not using jobs in BACKGROUND (Approach used for this setup)
 */
public class NetworkModule {

    /*
    @Singleton
    @Provides
    ChuckInterceptor provideChuckInterceptor(Context context) {
        return new ChuckInterceptor(context);
    }

    @Singleton
    @Provides
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(Timber::d);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Singleton
    @Provides
    Cache provideCache(App application) {
        int totalMbToCache = 2;
        int cacheSize = totalMbToCache * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                     StethoInterceptor stethoInterceptor,
                                     ChuckInterceptor chuckInterceptor,
                                     HttpHeaderInterceptor sendCredentialsInterceptor,
                                     Cache cache) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            clientBuilder
                    .addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
                    .addNetworkInterceptor(stethoInterceptor);
        }
        clientBuilder
                .addInterceptor(sendCredentialsInterceptor);

        if (BuildConfig.APP_ENDPOINT_USE_HTTP_CACHE) {
            clientBuilder
                    .cache(cache);
        }
        clientBuilder
                .connectTimeout(AppConstants.Network.HTTP_CONN_SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(AppConstants.Network.HTTP_READ_SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(AppConstants.Network.HTTP_WRITE_SECONDS_TIMEOUT, TimeUnit.SECONDS);

        return clientBuilder.build();
    }

    @Singleton
    @Provides
    HttpHeaderInterceptor provideSendCredentialsInterceptor(SharedPreferences sharedPreferences) {
        return new HttpHeaderInterceptor(sharedPreferences);
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit
                .Builder()
                .baseUrl(BuildConfig.APP_ENDPOINT_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    Gson provideGson() {
        final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        return new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
    */
}
