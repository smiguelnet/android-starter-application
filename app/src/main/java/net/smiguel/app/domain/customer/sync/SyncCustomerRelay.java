package net.smiguel.app.domain.customer.sync;

import com.jakewharton.rxrelay2.PublishRelay;

import net.smiguel.app.config.sync.SyncEventBus;
import net.smiguel.app.domain.entity.Customer;

import io.reactivex.Observable;

public class SyncCustomerRelay implements SyncEventBus<Customer> {

    private static SyncCustomerRelay INSTANCE;

    /**
     * PublishRelay is used when there are only one observer to check the data relayed (1..1)
     * ReplayRelay is used when there are more then one single observer to check the data relayed (1..*)
     */
    private PublishRelay<Customer> relay;

    public static SyncCustomerRelay getInstance() {
        if (INSTANCE == null) {
            synchronized (SyncCustomerRelay.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SyncCustomerRelay();
                }
            }
        }
        return INSTANCE;
    }

    public SyncCustomerRelay() {
        this.relay = PublishRelay.create();
    }

    public void accept(Customer customer) {
        relay.accept(customer);
    }

    public Observable<Customer> toObservable() {
        return relay;
    }
}
