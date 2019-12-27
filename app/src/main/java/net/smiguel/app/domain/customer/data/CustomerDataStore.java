package net.smiguel.app.domain.customer.data;

import net.smiguel.app.config.database.BaseDataStore;
import net.smiguel.app.domain.entity.Customer;

public interface CustomerDataStore extends BaseDataStore<Customer> {
    //Reserved for custom operations related with the entity <Customer>
}
