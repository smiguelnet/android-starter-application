package net.smiguel.app.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import net.smiguel.app.domain.customer.repository.CustomerRepository;
import net.smiguel.app.domain.entity.Customer;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class CustomerViewModel extends AndroidViewModel {

    private CustomerRepository mCustomerRepository;

    public CustomerViewModel(@NonNull Application application, CustomerRepository customerRepository) {
        super(application);
        this.mCustomerRepository = customerRepository;
    }

    public Single<Customer> save(Customer item) {
        return mCustomerRepository.save(item);
    }

    public Single<Customer> delete(Customer customer) {
        return mCustomerRepository.delete(customer);
    }

    public Maybe<Customer> get(Long id) {
        return mCustomerRepository.getAsync(id);
    }

    public Flowable<List<Customer>> getAll() {
        return mCustomerRepository.getAllAsync();
    }
}
