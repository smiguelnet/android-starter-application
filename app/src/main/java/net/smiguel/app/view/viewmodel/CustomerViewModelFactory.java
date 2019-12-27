package net.smiguel.app.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import net.smiguel.app.domain.customer.repository.CustomerRepository;

public class CustomerViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private final CustomerRepository mCustomerRepository;

    public CustomerViewModelFactory(Application mApplication, CustomerRepository mCustomerRepository) {
        this.mApplication = mApplication;
        this.mCustomerRepository = mCustomerRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CustomerViewModel.class)) {
            return (T) new CustomerViewModel(mApplication, mCustomerRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
