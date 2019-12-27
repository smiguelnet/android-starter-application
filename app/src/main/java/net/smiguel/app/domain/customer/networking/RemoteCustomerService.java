package net.smiguel.app.domain.customer.networking;

import android.support.annotation.WorkerThread;

import net.smiguel.app.config.networking.RemoteService;
import net.smiguel.app.domain.entity.Customer;
import net.smiguel.app.exception.ApplicationException;
import net.smiguel.app.exception.EndpointException;
import net.smiguel.app.util.NetworkUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import timber.log.Timber;

@WorkerThread
public class RemoteCustomerService {

    private static volatile RemoteCustomerService INSTANCE = null;

    public static RemoteCustomerService getInstance() {
        if (INSTANCE == null) {
            synchronized (RemoteCustomerService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteCustomerService();
                }
            }
        }
        return INSTANCE;
    }

    private RemoteCustomerEndpoint getRemoteCustomerEndpoint() {
        return RemoteService.getInstance().getRetrofit().create(RemoteCustomerEndpoint.class);
    }

    private CustomerDataMapper getCustomerDataMapper() {
        return CustomerDataMapper.getInstance();
    }

    //region Endpoint Operations

    public List<Customer> getAll() {
        try {
            Response<RemoteCustomerEndpoint.CustomerDataList> response = getRemoteCustomerEndpoint().getAll().execute();

            if (!NetworkUtils.isValidHttpBaseResponse(response)) {
                throw new EndpointException(response);
            }
            Timber.d("Successful remote response: %s", response.body());
            return getCustomerDataMapper().map(response.body().customers);

        } catch (IOException e) {
            Timber.e(e, "customer getAll error. " + e.getMessage());
            throw new ApplicationException("customer getAll error. " + e.getMessage(), e);
        }
    }

    public Customer get(Long id) {
        try {
            Response<RemoteCustomerEndpoint.CustomerData> response = getRemoteCustomerEndpoint().get(id).execute();

            if (!NetworkUtils.isValidHttpBaseResponse(response)) {
                throw new EndpointException(response);
            }
            Timber.d("Successful remote response: %s", response.body());
            return getCustomerDataMapper().map(response.body());

        } catch (IOException e) {
            Timber.e(e, "customer get error. " + e.getMessage());
            throw new ApplicationException("customer get error. " + e.getMessage(), e);
        }
    }

    public Customer delete(Customer customer) {
        try {
            Response<RemoteCustomerEndpoint.CustomerData> response = getRemoteCustomerEndpoint().delete(customer.getIdRef()).execute();

            if (!NetworkUtils.isValidHttpBaseResponse(response)) {
                throw new EndpointException(response);
            }
            Timber.d("Successful remote response: %s", response.body());
            return getCustomerDataMapper().map(response.body());

        } catch (IOException e) {
            Timber.e(e, "customer delete error. " + e.getMessage());
            throw new ApplicationException("customer delete error. " + e.getMessage(), e);
        }
    }

    public Customer create(Customer customer) {
        return save(customer, true);
    }

    public Customer update(Customer customer) {
        return save(customer, false);
    }

    private Customer save(Customer customer, boolean newCustomer) {
        try {
            RemoteCustomerEndpoint.CustomerData customerData = getCustomerDataMapper().reverseMap(customer);

            Response<RemoteCustomerEndpoint.CustomerData> response;
            if (newCustomer) {
                response = getRemoteCustomerEndpoint().create(customerData).execute();
            } else {
                response = getRemoteCustomerEndpoint().update(customerData).execute();
            }

            if (!NetworkUtils.isValidHttpBaseResponse(response)) {
                throw new EndpointException(response);
            }
            return getCustomerDataMapper().map(response.body());

        } catch (IOException e) {
            Timber.e(e, "customer save error. " + e.getMessage());
            throw new ApplicationException("customer save error. " + e.getMessage(), e);
        }
    }
    //endregion
}
