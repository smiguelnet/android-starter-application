package net.smiguel.app.config.database;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface BaseDataStore<T> {

    T insert(T customer);

    T update(T customer);

    T delete(T customer);

    T get(Long id);

    Maybe<T> getAsync(Long id);

    List<T> getAll();

    Flowable<List<T>> getAllAsync();

    T getByIdRef(Long id);

    Maybe<T> getByIdRefAsync(Long id);
}
