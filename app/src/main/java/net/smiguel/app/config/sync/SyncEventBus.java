package net.smiguel.app.config.sync;

import io.reactivex.Observable;

public interface SyncEventBus<T> {

    void accept(T item);

    Observable<T> toObservable();
}
