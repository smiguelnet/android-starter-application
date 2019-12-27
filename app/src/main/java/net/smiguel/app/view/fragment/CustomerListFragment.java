package net.smiguel.app.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.smiguel.app.R;
import net.smiguel.app.domain.customer.sync.SyncCustomerLifecycleObserver;
import net.smiguel.app.domain.entity.Customer;
import net.smiguel.app.view.adapter.CustomerListAdapter;
import net.smiguel.app.view.adapter.callback.ListItemTouchHelper;
import net.smiguel.app.view.adapter.decoration.ListItemDecoration;
import net.smiguel.app.view.viewmodel.CustomerViewModel;
import net.smiguel.app.view.viewmodel.CustomerViewModelFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CustomerListFragment extends BaseFragment {

    public static final String BUNDLE_CUSTOMER_ID = "CUSTOMER_ID";

    @BindView(R.id.customerRecyclerView)
    RecyclerView mCustomerRecyclerView;
    RecyclerView.Adapter mCustomerAdapter;
    RecyclerView.LayoutManager mCustomerLayoutManager;

    @Inject
    CustomerViewModelFactory mCustomerViewModelFactory;
    CustomerViewModel mCustomerViewModel;
    long customerIdToMark;

    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.fabAdd)
    FloatingActionButton mFloatingActionButton;

    @Inject
    SyncCustomerLifecycleObserver mCustomerLifecycleObserver;

    public static CustomerListFragment newInstance() {
        Bundle args = new Bundle();
        CustomerListFragment fragment = new CustomerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        addObserver(mCustomerLifecycleObserver);

        setViewModel();
        setViewEvents();
        observeData(view);
        setTitle(getString(R.string.customer_list_title));

        if (getArguments() != null) {
            customerIdToMark = getArguments().getLong(CustomerListFragment.BUNDLE_CUSTOMER_ID);
        }
        return view;
    }

    private void setViewModel() {
        mCustomerViewModel = ViewModelProviders.of(this, mCustomerViewModelFactory).get(CustomerViewModel.class);
    }

    private void setViewEvents() {
        mFloatingActionButton.setOnClickListener(view1 -> {
            customerIdToMark = 0;
            addFragment(CustomerFormFragment.newInstance());
        });
    }

    private void observeData(View view) {
        mCompositeDisposable.add(mCustomerViewModel.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(customers -> setRecyclerView(view, customers),
                        throwable -> Timber.e(throwable.getMessage()))
        );
    }

    private void setRecyclerView(View view, List<Customer> customers) {
        //Set layout
        mCustomerLayoutManager = new LinearLayoutManager(view.getContext());
        mCustomerRecyclerView.setLayoutManager(mCustomerLayoutManager);
        //Set data and selection event
        mCustomerAdapter = new CustomerListAdapter(customers, v -> customerListClickListener(v), customerIdToMark);
        mCustomerRecyclerView.setAdapter(mCustomerAdapter);
        mCustomerRecyclerView.addItemDecoration(new ListItemDecoration(getContext()));
        mCustomerRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ListItemTouchHelper swipeToDeleteCallback = new ListItemTouchHelper(0, ItemTouchHelper.LEFT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                CustomerListAdapter customerListAdapter = (CustomerListAdapter) mCustomerRecyclerView.getAdapter();
                int selectedPosition = viewHolder.getAdapterPosition();

                if (selectedPosition > -1) {
                    Customer customer = customerListAdapter.getItemSelected(selectedPosition);
                    if (customer != null) {
                        deleteCustomer(customer, customerListAdapter, selectedPosition);
                    }
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(mCustomerRecyclerView);
    }

    private void customerListClickListener(View view) {
        CustomerListAdapter customerListAdapter = (CustomerListAdapter) mCustomerRecyclerView.getAdapter();

        ConstraintLayout layout = view.findViewById(R.id.customerLayoutView);
        int selectedPosition = (int) layout.getTag();

        Customer customer = customerListAdapter.getItemSelected(selectedPosition);

        CustomerFormFragment customerFormFragment = CustomerFormFragment.newInstance();

        if (customer != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(BUNDLE_CUSTOMER_ID, customer.getId());
            customerFormFragment.setArguments(bundle);
        }
        addFragment(customerFormFragment);
    }

    private void deleteCustomer(Customer customer, CustomerListAdapter customerListAdapter, int selectedPosition) {
        mCompositeDisposable.add(mCustomerViewModel
                .delete(customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    Timber.d("Local customer has been deleted and wait for synchronization");
                    customerListAdapter.removeAt(selectedPosition);
                }, throwable -> {
                    Timber.e("An error occurs while deleting local customer");
                })
        );
    }
}
