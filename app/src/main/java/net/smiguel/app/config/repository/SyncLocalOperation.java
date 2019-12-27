package net.smiguel.app.config.repository;

public interface SyncLocalOperation<T> {
    void syncWithLocal(T item);
}
