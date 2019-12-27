package net.smiguel.app.domain.customer.networking;

import net.smiguel.app.config.networking.DataMapper;
import net.smiguel.app.domain.entity.Customer;

public class CustomerDataMapper extends DataMapper<RemoteCustomerEndpoint.CustomerData, Customer> {

    private static volatile CustomerDataMapper INSTANCE;

    public static CustomerDataMapper getInstance() {
        if (INSTANCE == null) {
            synchronized (CustomerDataMapper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CustomerDataMapper();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Customer map(RemoteCustomerEndpoint.CustomerData item) {
        if (item == null) {
            return null;
        }
        return new Customer(0,
                item.idRef,
                item.insertDate,
                item.updateDate,
                item.active,
                null,
                null,
                false,
                item.name,
                item.email,
                item.birthday);
    }

    @Override
    public RemoteCustomerEndpoint.CustomerData reverseMap(Customer item) {
        if (item == null) {
            return null;
        }
        return new RemoteCustomerEndpoint.CustomerData(
                item.getIdRef(),
                item.getInsertDate(),
                item.getUpdateDate(),
                item.isActive(),
                item.getName(),
                item.getEmail(),
                item.getBirthday());
    }
}
