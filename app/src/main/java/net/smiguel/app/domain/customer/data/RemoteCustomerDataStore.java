package net.smiguel.app.domain.customer.data;

import net.smiguel.app.config.repository.SyncRemoteOperation;
import net.smiguel.app.config.repository.SyncRepository;
import net.smiguel.app.config.sync.JobManagerFactory;
import net.smiguel.app.domain.customer.networking.RemoteCustomerService;
import net.smiguel.app.domain.customer.sync.SyncCustomerJob;
import net.smiguel.app.domain.entity.Customer;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class RemoteCustomerDataStore implements CustomerDataStore, SyncRemoteOperation<Customer> {

    private RemoteCustomerService mRemoteCustomerService;

    public RemoteCustomerDataStore() {
        this.mRemoteCustomerService = RemoteCustomerService.getInstance();
    }

    @Override
    public Customer insert(Customer customer) {
        return mRemoteCustomerService.create(customer);
    }

    @Override
    public Customer update(Customer customer) {
        return mRemoteCustomerService.update(customer);
    }

    @Override
    public Customer delete(Customer customer) {
        return mRemoteCustomerService.delete(customer);
    }

    @Override
    public Customer get(Long id) {
        return mRemoteCustomerService.get(id);
    }

    @Override
    public Maybe<Customer> getAsync(Long id) {
        return Maybe.fromCallable(() -> get(id));
    }

    @Override
    public List<Customer> getAll() {
        return mRemoteCustomerService.getAll();
    }

    @Override
    public Flowable<List<Customer>> getAllAsync() {
        return Flowable.fromCallable(() -> getAll());
    }

    @Override
    public Customer getByIdRef(Long id) {
        throw new UnsupportedOperationException("Operation getByIdRef not available");
    }

    @Override
    public Maybe<Customer> getByIdRefAsync(Long id) {
        throw new UnsupportedOperationException("Operation getByIdRef not available");
    }

    @Override
    public void syncWithServer(Customer customer, SyncRepository.EntityAction entityAction) {
        JobManagerFactory.getJobManager().addJobInBackground(new SyncCustomerJob(customer, entityAction));
    }
}
