package net.smiguel.app.domain.customer.sync;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.birbit.android.jobqueue.RetryConstraint;

import net.smiguel.app.config.sync.SyncJob;
import net.smiguel.app.config.repository.SyncRepository;
import net.smiguel.app.constants.AppConstants;
import net.smiguel.app.domain.customer.networking.RemoteCustomerService;
import net.smiguel.app.domain.entity.Customer;
import net.smiguel.app.exception.EndpointException;

import timber.log.Timber;

@WorkerThread
public class SyncCustomerJob extends SyncJob {

    private static final String TAG = SyncCustomerJob.class.getCanonicalName();
    private static final String TAG_ENTITY = Customer.class.getCanonicalName();

    private Customer mCustomer = null;
    private SyncRepository.EntityAction mEntityAction;

    public SyncCustomerJob(Customer mCustomer, SyncRepository.EntityAction mEntityAction) {
        this.mCustomer = mCustomer;
        this.mEntityAction = mEntityAction;
    }

    @Override
    public void onAdded() {
        Timber.d("%s Job executing onAdded() for %s ", TAG, TAG_ENTITY);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("%s Job executing onRun() for %s ", TAG, TAG_ENTITY);
         if (mCustomer != null) {
            Customer customer = null;

            switch (mEntityAction) {
                case CREATE:
                    customer = RemoteCustomerService.getInstance().create(mCustomer);
                    break;
                case UPDATE:
                    customer = RemoteCustomerService.getInstance().update(mCustomer);
                    break;
                case DELETE:
                    customer = RemoteCustomerService.getInstance().delete(mCustomer);
                    break;
                default:
                    Timber.d("%s Job executing onRun() - Invalid action for job %s - action %s", TAG, TAG_ENTITY, mEntityAction.toString());
            }

            if (customer != null) {
                Timber.d("%s Job executing onRun() - Customer has been saved successfully %s  - action %s", TAG, TAG_ENTITY, mEntityAction.toString());
                customer.setId(mCustomer.getId());
                customer.setInsertLocalDate(mCustomer.getInsertLocalDate());
                customer.setUpdateLocalDate(mCustomer.getUpdateLocalDate());
                SyncCustomerRelay.getInstance().accept(customer);
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Timber.d("%s Job executing cancelReason() for %s ", TAG, TAG_ENTITY);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        Timber.d("%s Job executing shouldReRunOnThrowable() for %s ", TAG, TAG_ENTITY);
        if (throwable instanceof EndpointException) {
            EndpointException endpointException = (EndpointException) throwable;
            if (endpointException.getResponse() != null) {
                int httpStatusCode = endpointException.getResponse().code();
                Timber.d("%s Job executing shouldReRunOnThrowable(). Http Error code: %d", TAG, httpStatusCode);

                if (httpStatusCode >= 400 && httpStatusCode < 500) {
                    return RetryConstraint.CANCEL;
                }
            }
        }
        if (runCount > AppConstants.Jobs.EXECUTE_SYNC_JOB_MAX_ATTEMPT) {
            Timber.d("%s Job executing shouldReRunOnThrowable(). Max attempt has achieved: %d", TAG, AppConstants.Jobs.EXECUTE_SYNC_JOB_MAX_ATTEMPT);
            return RetryConstraint.CANCEL;
        }
        SystemClock.sleep(AppConstants.Jobs.EXECUTE_SYNC_JOB_MILLISECONDS);
        return RetryConstraint.RETRY;
    }
}
