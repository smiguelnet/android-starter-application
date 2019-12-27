package net.smiguel.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.facebook.stetho.Stetho;

import net.smiguel.app.config.database.AppDatabase;
import net.smiguel.app.config.injection.component.DaggerAppComponent;
import net.smiguel.app.config.sync.JobManagerFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;


public class App extends Application implements HasServiceInjector, HasActivityInjector, HasSupportFragmentInjector, HasBroadcastReceiverInjector {

    private static Application sApplication;

    @Inject
    DispatchingAndroidInjector<Activity> mActivityDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Service> mDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<BroadcastReceiver> mBroadcastReceiverAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        AppDatabase.init(this);

        JobManagerFactory.initialize(getApplication().getApplicationContext());
    }

    public static Context getApplication() {
        return sApplication;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mActivityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return mDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return mBroadcastReceiverAndroidInjector;
    }
}
