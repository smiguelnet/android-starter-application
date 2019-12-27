package net.smiguel.app.config.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import net.smiguel.app.constants.AppConstants;
import net.smiguel.app.domain.customer.data.CustomerDao;
import net.smiguel.app.domain.entity.Customer;

import timber.log.Timber;

@Database(entities = {Customer.class},
        version = AppConstants.Database.DATABASE_VERSION,
        exportSchema = false)
@TypeConverters(value = {TypeConverterHelper.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            init(context);
        }
        return INSTANCE;
    }

    public synchronized static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppConstants.Database.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            if (db != null) {
                                Timber.d("Database %s has been creates successfully", AppConstants.Database.DATABASE_NAME);
                                //Pre-populate the database [HERE IF NECESSARY]
                            }
                        }
                    })
                    .build();
        } else {
            Timber.d("Database %s INSTANCE has already been instantiated", AppConstants.Database.DATABASE_NAME);
        }
    }

    //region Daos
    public abstract CustomerDao customerDao();
    //endregion
}
