package net.smiguel.app.domain.customer.repository;

import android.support.annotation.WorkerThread;

import net.smiguel.app.config.repository.SyncRepository;
import net.smiguel.app.domain.customer.data.LocalCustomerDataStore;
import net.smiguel.app.domain.customer.data.RemoteCustomerDataStore;
import net.smiguel.app.domain.entity.Customer;
import net.smiguel.app.exception.ApplicationException;
import net.smiguel.app.util.DataUtils;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@WorkerThread
public class CustomerRepository implements SyncRepository<Customer> {

    private LocalCustomerDataStore mLocalCustomerDataStore;
    private RemoteCustomerDataStore mRemoteCustomerDataStore;

    public CustomerRepository(LocalCustomerDataStore mLocalCustomerDataStore, RemoteCustomerDataStore mRemoteCustomerDataStore) {
        this.mLocalCustomerDataStore = mLocalCustomerDataStore;
        this.mRemoteCustomerDataStore = mRemoteCustomerDataStore;
    }

    //region Foreground Operations
    @Override
    public Single<Customer> save(Customer item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalid customer to save");
        }
        if (item.getId() == 0) {
            return insert(item);
        } else {
            return update(item);
        }
    }

    private Single<Customer> insert(Customer item) {
        item.setInsertLocalDate(DataUtils.getCurrentDate());
        item.setSyncPending(true);
        item.setActive(true);

        return Single
                .fromCallable(() -> mLocalCustomerDataStore.insert(item))
                .doAfterSuccess(customer -> syncWithServer(customer, EntityAction.CREATE));
    }

    private Single<Customer> update(Customer item) {
        item.setUpdateLocalDate(DataUtils.getCurrentDate());
        item.setSyncPending(true);

        return Single
                .fromCallable(() -> mLocalCustomerDataStore.update(item))
                .doAfterSuccess(customer -> {
                    if (customer != null) {
                        if (customer.getIdRef() != null) {
                            syncWithServer(customer, EntityAction.UPDATE);
                        } else {
                            syncWithServer(customer, EntityAction.CREATE);
                        }
                    }
                });
    }

    @Override
    public Single<Customer> delete(Customer item) {
        item.setUpdateLocalDate(DataUtils.getCurrentDate());
        item.setSyncPending(true);
        item.setActive(false);

        return Single
                .fromCallable(() -> mLocalCustomerDataStore.delete(item))
                .doAfterSuccess(customer -> {
                    if (customer != null && customer.getIdRef() != null) {
                        syncWithServer(customer, EntityAction.DELETE);
                    }
                });
    }

    @Override
    public Customer get(Long id) {
        return mLocalCustomerDataStore.get(id);
    }

    @Override
    public Maybe<Customer> getAsync(Long id) {
        return mLocalCustomerDataStore.getAsync(id);
    }

    @Override
    public List<Customer> getAll() {
        return mLocalCustomerDataStore.getAll();
    }

    @Override
    public Flowable<List<Customer>> getAllAsync() {
        return mLocalCustomerDataStore.getAllAsync();
    }
    //endregion

    //region Background Operations
    @Override
    public void syncWithServer(Customer item, EntityAction entityAction) {
        mRemoteCustomerDataStore.syncWithServer(item, entityAction);
    }

    @Override
    public void syncWithLocal(Customer item) {
        if (item.getId() == 0) {
            Customer customerIdRef = mLocalCustomerDataStore.getByIdRef(item.getIdRef());
            if (customerIdRef == null) {
                throw new ApplicationException("Customer Reference ID not found");
            }
            item.setId(customerIdRef.getId());
        }
        item.setUpdateLocalDate(DataUtils.getCurrentDate());
        item.setSyncPending(false);
        mLocalCustomerDataStore.update(item);
    }
    //endregion
}
