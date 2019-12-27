package net.smiguel.app.config.sync;

import android.content.Context;
import android.os.Build;

import com.birbit.android.jobqueue.BuildConfig;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import timber.log.Timber;

public class JobManagerFactory {

    private static volatile JobManager INSTANCE;

    public static JobManager getJobManager() {
        if (INSTANCE == null) {
            throw new NullPointerException("JobManager has not been initialized. Check getJobManager(Context context) operation");
        }
        return INSTANCE;
    }

    public static synchronized void initialize(Context context) {
        if (INSTANCE == null) {
            synchronized (JobManagerFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = configureJobManager(context);
                }
            }
        }
    }

    private static CustomLogger customLogger = new CustomLogger() {
        @Override
        public boolean isDebugEnabled() {
            return BuildConfig.DEBUG;
        }

        @Override
        public void d(String text, Object... args) {
            Timber.d(String.format(text, args));
        }

        @Override
        public void e(Throwable t, String text, Object... args) {
            Timber.e(t, String.format(text, args));
        }

        @Override
        public void e(String text, Object... args) {
            Timber.e(String.format(text, args));
        }

        @Override
        public void v(String text, Object... args) {
            Timber.v(String.format(text, args));
        }
    };

    private static JobManager configureJobManager(Context context) {
        final int MIN_NUM_THREAD_ALIVE = 5;
        final int MAX_NUM_THREAD = 10;
        final int MAX_NUM_JOBS_PER_THREAD = 5;
        final boolean BATCH_SCHEDULE_JOB = false;

        Configuration.Builder builder = new Configuration.Builder(context);
        builder
                .minConsumerCount(MIN_NUM_THREAD_ALIVE)
                .maxConsumerCount(MAX_NUM_THREAD)
                .loadFactor(MAX_NUM_JOBS_PER_THREAD)
                .customLogger(customLogger);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Timber.d("JobManager factory creates a JobSchedulerService instance ( >= API 21)");
            builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(context, JobSchedulerService.class), BATCH_SCHEDULE_JOB);

        } else {
            int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
            if (enableGcm == ConnectionResult.SUCCESS) {
                Timber.d("JobManager factory creates a GcmJobSchedulerService instance ( < API 21)");
                builder.scheduler(GcmJobSchedulerService.createSchedulerFor(context, GcmJobSchedulerService.class), BATCH_SCHEDULE_JOB);
            } else {
                Timber.d("JobManager factory did not identify a suitable SchedulerService. There is no GoogleApiAvailability available");
            }
        }
        return new JobManager(builder.build());
    }
}
