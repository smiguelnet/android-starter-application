package net.smiguel.app.config.injection.module;

import android.content.Context;
import android.content.SharedPreferences;

import net.smiguel.app.App;
import net.smiguel.app.config.database.AppDatabase;
import net.smiguel.app.constants.AppConstants;
import net.smiguel.app.domain.customer.data.CustomerDao;
import net.smiguel.app.domain.customer.data.LocalCustomerDataStore;
import net.smiguel.app.domain.customer.data.RemoteCustomerDataStore;
import net.smiguel.app.domain.customer.repository.CustomerRepository;
import net.smiguel.app.domain.customer.sync.SyncCustomerLifecycleObserver;
import net.smiguel.app.view.viewmodel.CustomerViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Provides
    SharedPreferences provideSharedPreference(Context context) {
        return context.getSharedPreferences(AppConstants.SharedData.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    //region CustomerData
    @Singleton
    @Provides
    CustomerDao provideCustomerDao(Context context) {
        return AppDatabase.getInstance(context).customerDao();
    }

    @Singleton
    @Provides
    LocalCustomerDataStore provideLocalCustomerDataStore(CustomerDao customerDao) {
        return new LocalCustomerDataStore(customerDao);
    }

    @Singleton
    @Provides
    RemoteCustomerDataStore provideRemoteCustomerDataStore() {
        return new RemoteCustomerDataStore();
    }

    @Singleton
    @Provides
    CustomerRepository provideCustomerRepository(LocalCustomerDataStore localCustomerDataStore, RemoteCustomerDataStore remoteCustomerDataStore) {
        return new CustomerRepository(localCustomerDataStore, remoteCustomerDataStore);
    }

    @Singleton
    @Provides
    SyncCustomerLifecycleObserver provideCustomerLifecycleObserver(CustomerRepository customerRepository) {
        return new SyncCustomerLifecycleObserver(customerRepository);
    }

    @Singleton
    @Provides
    CustomerViewModelFactory provideCustomerViewModel(App application, CustomerRepository customerRepository) {
        return new CustomerViewModelFactory(application, customerRepository);
    }

    //IMPORTANT: RemoteCustomerService will be created by Local Singleton Implementation in order to Job recovery after device boot

    //endregion
}
