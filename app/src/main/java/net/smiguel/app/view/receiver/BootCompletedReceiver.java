package net.smiguel.app.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.smiguel.app.domain.customer.repository.CustomerRepository;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Inject
    CustomerRepository mCustomerRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        setupInjection(context);

        Timber.d("application initialized... - " + mCustomerRepository);
    }

    private void setupInjection(Context context) {
        AndroidInjection.inject(this, context);
    }
}
