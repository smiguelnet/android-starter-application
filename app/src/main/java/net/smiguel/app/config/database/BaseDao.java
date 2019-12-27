package net.smiguel.app.config.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(T item);

    @Delete
    void delete(T item);
}
