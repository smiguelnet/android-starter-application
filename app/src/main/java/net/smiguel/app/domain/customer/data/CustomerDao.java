package net.smiguel.app.domain.customer.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import net.smiguel.app.config.database.BaseDao;
import net.smiguel.app.constants.AppConstants;
import net.smiguel.app.domain.entity.Customer;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface CustomerDao extends BaseDao<Customer> {

    //region Sync
    @Query("SELECT * FROM  " + AppConstants.Database.TABLE_CUSTOMER + " WHERE " + AppConstants.Database.COL_ID + " = :id")
    Customer get(Long id);

    @Query("SELECT * FROM  " + AppConstants.Database.TABLE_CUSTOMER + " WHERE " + AppConstants.Database.COL_ID_REF + " = :id")
    Customer getByIdRef(Long id);

    @Query("SELECT * FROM " + AppConstants.Database.TABLE_CUSTOMER)
    List<Customer> getAll();
    //endregion

    //region Async
    @Query("SELECT * FROM  " + AppConstants.Database.TABLE_CUSTOMER + " WHERE " + AppConstants.Database.COL_ID + " = :id")
    Maybe<Customer> getAsync(Long id);

    @Query("SELECT * FROM  " + AppConstants.Database.TABLE_CUSTOMER + " WHERE " + AppConstants.Database.COL_ID_REF + " = :id")
    Maybe<Customer> getByIdRefAsync(Long id);

    @Query("SELECT * FROM " + AppConstants.Database.TABLE_CUSTOMER)
    Flowable<List<Customer>> getAllAsync();
    //region
}
