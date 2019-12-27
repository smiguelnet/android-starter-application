package net.smiguel.app.config.sync;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;

public class JobSchedulerService extends FrameworkJobSchedulerService {

    @NonNull
    @Override
    protected JobManager getJobManager() {
        return JobManagerFactory.getJobManager();
    }
}
