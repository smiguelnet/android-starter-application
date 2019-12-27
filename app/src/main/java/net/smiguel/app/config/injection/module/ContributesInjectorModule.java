package net.smiguel.app.config.injection.module;

import net.smiguel.app.view.activity.LoginActivity;
import net.smiguel.app.view.activity.MainActivity;
import net.smiguel.app.view.fragment.CustomerFormFragment;
import net.smiguel.app.view.fragment.CustomerListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ContributesInjectorModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector
    abstract CustomerListFragment contributeCustomerListFragment();

    @ContributesAndroidInjector
    abstract CustomerFormFragment contributeCustomerFormFragment();
}
