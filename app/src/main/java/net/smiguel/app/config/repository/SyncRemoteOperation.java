package net.smiguel.app.config.repository;

public interface SyncRemoteOperation<T> {
    void syncWithServer(T item, SyncRepository.EntityAction entityAction);
}
