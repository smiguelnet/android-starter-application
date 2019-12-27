package net.smiguel.app.config.sync;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;

public abstract class SyncJob extends Job {

    private static final String TAG = SyncJob.class.getCanonicalName();

    protected SyncJob() {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .groupBy(TAG)
                .persist());
    }
}
