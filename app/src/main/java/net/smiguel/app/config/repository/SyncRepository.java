package net.smiguel.app.config.repository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface SyncRepository<T> extends SyncRemoteOperation<T>, SyncLocalOperation<T> {

    enum EntityAction {
        CREATE, UPDATE, DELETE
    }

    Single<T> save(T item);

    Single<T> delete(T item);

    T get(Long id);

    Maybe<T> getAsync(Long id);

    List<T> getAll();

    Flowable<List<T>> getAllAsync();
}
