package net.smiguel.app.domain.customer.data;

import net.smiguel.app.domain.entity.Customer;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class LocalCustomerDataStore implements CustomerDataStore {

    private CustomerDao mCustomerDao;

    public LocalCustomerDataStore(CustomerDao mCustomerDao) {
        this.mCustomerDao = mCustomerDao;
    }

    @Override
    public Customer insert(Customer customer) {
        long rowId = mCustomerDao.insert(customer);
        customer.setId(rowId);
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        mCustomerDao.update(customer);
        return customer;
    }

    @Override
    public Customer delete(Customer customer) {
        mCustomerDao.delete(customer);
        return customer;
    }

    @Override
    public Customer get(Long id) {
        return mCustomerDao.get(id);
    }

    @Override
    public Maybe<Customer> getAsync(Long id) {
        return mCustomerDao.getAsync(id);
    }

    @Override
    public List<Customer> getAll() {
        return mCustomerDao.getAll();
    }

    @Override
    public Flowable<List<Customer>> getAllAsync() {
        return mCustomerDao.getAllAsync();
    }

    @Override
    public Customer getByIdRef(Long id) {
        return mCustomerDao.getByIdRef(id);
    }

    @Override
    public Maybe<Customer> getByIdRefAsync(Long id) {
        return mCustomerDao.getByIdRefAsync(id);
    }
}
