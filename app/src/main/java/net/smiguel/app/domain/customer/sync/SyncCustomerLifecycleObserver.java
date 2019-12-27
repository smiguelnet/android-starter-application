package net.smiguel.app.domain.customer.sync;

import net.smiguel.app.config.sync.SyncLifecycleObserver;
import net.smiguel.app.domain.customer.repository.CustomerRepository;
import net.smiguel.app.domain.entity.Customer;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SyncCustomerLifecycleObserver extends SyncLifecycleObserver<Customer> {

    private CustomerRepository mCustomerRepository;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    public SyncCustomerLifecycleObserver(CustomerRepository mCustomerRepository) {
        this.mCustomerRepository = mCustomerRepository;
    }

    public void onResume() {
        Timber.d("Lifecycle observer - onResume");
        mDisposables.add(
                SyncCustomerRelay
                        .getInstance()
                        .toObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                customer -> syncWithLocal(customer),
                                throwable -> Timber.e("error occurs when saving local after rest operation customer: " + throwable.getMessage())
                        ));
    }

    public void syncWithLocal(Customer customer) {
        Timber.d("Lifecycle observer - syncWithLocal ------------------");
        mDisposables.add(Completable
                .fromAction(() -> mCustomerRepository.syncWithLocal(customer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Timber.d("local customer update after rest operation has been done"),
                        throwable -> {
                            Timber.e("error occurs when saving local after rest operation customer: " + throwable.getMessage());
                            showNotification(customer, throwable);
                        })
        );
    }

    @Override
    public void showNotification(Customer item, Throwable throwable) {
        String channelId = "CustomerSync";
        String channelName = "CustomerChannel";
        String title = "Customer Synchronization";
        String description = null;

        if (throwable != null) {
            description = "Error while sync customer";
        } else {
            description = String.format("Customer [%d] has been updated successfully", item.getIdRef());
        }
        showNotification(channelId, channelName, title, description);
    }

    public void onPause() {
        Timber.d("Lifecycle observer - onPause");
        mDisposables.clear();
    }

    public void onDestroy() {
        Timber.d("Lifecycle observer - onDestroy");
        mDisposables.dispose();
    }
}
