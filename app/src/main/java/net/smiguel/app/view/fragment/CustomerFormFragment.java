package net.smiguel.app.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import net.smiguel.app.R;
import net.smiguel.app.domain.entity.Customer;
import net.smiguel.app.util.DataUtils;
import net.smiguel.app.util.ViewUtils;
import net.smiguel.app.view.viewmodel.CustomerViewModel;
import net.smiguel.app.view.viewmodel.CustomerViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CustomerFormFragment extends BaseFragment {

    @BindView(R.id.txtId)
    TextView txtId;

    @BindView(R.id.txtIdRef)
    TextView txtIdRef;

    @BindView(R.id.txtInsertDate)
    TextView txtInsertDate;

    @BindView(R.id.txtUpdateDate)
    TextView txtUpdateDate;

    @BindView(R.id.txtInsertLocalDate)
    TextView txtInsertLocalDate;

    @BindView(R.id.txtUpdateLocalDate)
    TextView txtUpdateLocalDate;

    @BindView(R.id.txt_name)
    EditText txtName;

    @BindView(R.id.txt_email)
    EditText txtEmail;

    @BindView(R.id.txt_birthday)
    EditText txtBirthday;

    @BindView(R.id.switch_status)
    Switch switchStatus;

    @BindView(R.id.btn_add_customer)
    Button btnAddCustomer;

    @BindView(R.id.traceTitle)
    TextView txtTraceTitle;

    @BindView(R.id.traceArea)
    LinearLayout traceArea;

    @Inject
    CustomerViewModelFactory mCustomerViewModelFactory;
    CustomerViewModel mCustomerViewModel;
    Customer mCustomer;

    public static CustomerFormFragment newInstance() {
        Bundle args = new Bundle();
        CustomerFormFragment fragment = new CustomerFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_customer_form, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        setViewModel();
        setViewEvents();

        if (getArguments() != null && getArguments().getLong(CustomerListFragment.BUNDLE_CUSTOMER_ID) > 0) {
            setTitle(getString(R.string.customer_update_title));
            setHasOptionsMenu(true);
            showTraceArea(true);
            mCompositeDisposable.add(
                    mCustomerViewModel
                            .get(getArguments().getLong(CustomerListFragment.BUNDLE_CUSTOMER_ID))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(customer -> {
                                        mCustomer = customer;
                                        fillCustomerForm();
                                    },
                                    throwable -> {
                                        Timber.e(throwable);
                                        resetCustomerForm();
                                        showMessage(throwable.getMessage());
                                    }));
        } else {
            setTitle(getString(R.string.customer_add_title));
            setHasOptionsMenu(false);
            showTraceArea(false);
            resetCustomerForm();
            mCustomer = new Customer();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_customer_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_customer_delete:
                deleteCustomer(mCustomer);
                break;
            default:
                break;
        }
        return true;
    }

    private void showTraceArea(boolean show) {
        if (show) {
            txtTraceTitle.setVisibility(View.VISIBLE);
            traceArea.setVisibility(View.VISIBLE);
        } else {
            txtTraceTitle.setVisibility(View.GONE);
            traceArea.setVisibility(View.GONE);
        }
    }

    private void setViewModel() {
        mCustomerViewModel = ViewModelProviders.of(this, mCustomerViewModelFactory).get(CustomerViewModel.class);
    }

    private void setViewEvents() {
        btnAddCustomer.setOnClickListener(v -> {
            Customer customer = getCustomer();
            if (customer != null && isFormValid()) {
                saveCustomer(customer);
            }
        });

        txtBirthday.addTextChangedListener(new ViewUtils.DateMaskWatcher(txtBirthday));
    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(txtName.getText().toString())) {
            txtName.setError(getString(R.string.error_required_field));
            txtName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(txtEmail.getText().toString())) {
            txtEmail.setError(getString(R.string.error_required_field));
            txtEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(txtBirthday.getText().toString())) {
            txtBirthday.setError(getString(R.string.error_required_field));
            txtBirthday.requestFocus();
            return false;
        }

        if (!DataUtils.isValidDate(txtBirthday.getText().toString())) {
            txtBirthday.setError(getString(R.string.error_invalid_field));
            txtBirthday.requestFocus();
            return false;
        }
        return true;
    }

    private void fillCustomerForm() {
        resetCustomerForm();

        if (mCustomer == null || mCustomer.getId() == 0) {
            return;
        }

        txtName.setText(mCustomer.getName());
        txtEmail.setText(mCustomer.getEmail());
        txtBirthday.setText(DataUtils.formatDate(mCustomer.getBirthday()));
        switchStatus.setChecked(mCustomer.isActive());

        //Trace
        txtId.setText(String.valueOf(mCustomer.getId()));

        if (mCustomer.getIdRef() != null) {
            txtIdRef.setText(String.valueOf(mCustomer.getIdRef()));
        }

        txtInsertDate.setText(DataUtils.formatDatetime(mCustomer.getInsertDate()));
        txtUpdateDate.setText(DataUtils.formatDatetime(mCustomer.getUpdateDate()));

        txtInsertLocalDate.setText(DataUtils.formatDatetime(mCustomer.getInsertLocalDate()));
        txtUpdateLocalDate.setText(DataUtils.formatDatetime(mCustomer.getUpdateLocalDate()));
    }

    private void resetCustomerForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtBirthday.setText("");
        switchStatus.setChecked(false);

        txtName.requestFocus();
        resetTrace();
        ViewUtils.hideKeyboard(getActivity());
    }

    private void resetTrace() {
        txtId.setText("");
        txtIdRef.setText("");
        txtInsertDate.setText("");
        txtUpdateDate.setText("");
        txtInsertLocalDate.setText("");
        txtUpdateLocalDate.setText("");
    }

    private Customer getCustomer() {
        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        String birthday = txtBirthday.getText().toString();

        mCustomer.setName(name);
        mCustomer.setEmail(email);
        mCustomer.setBirthday(DataUtils.parseDate(birthday));
        mCustomer.setActive(switchStatus.isChecked());

        return mCustomer;
    }

    private void deleteCustomer(Customer customer) {
        if (customer == null) {
            showMessage("There is no valid customer to be deleted");
            return;
        }

        mCompositeDisposable.add(mCustomerViewModel
                .delete(customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    Timber.d("Local customer has been deleted and wait for synchronization");
                    popUp();
                }, throwable -> {
                    Timber.e("An error occurs while deleting local customer");
                    showMessage("An error occurs while deleting local customer. Please try again");
                }));
    }

    private void saveCustomer(Customer customer) {
        mCompositeDisposable.add(mCustomerViewModel
                .save(customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> showAlertMessage("Customer", "Customer has been saved successfully", v -> popUp(this.getClass().getSimpleName())),
                        throwable -> {
                            Timber.e("Error occurred while saving the customer data. " + throwable.getMessage());
                            showAlertMessage("Error occurred while saving the customer data. Please try again", throwable.getMessage());
                        }));
    }
}