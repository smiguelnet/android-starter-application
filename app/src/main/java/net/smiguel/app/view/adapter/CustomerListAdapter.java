package net.smiguel.app.view.adapter;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.smiguel.app.R;
import net.smiguel.app.domain.entity.Customer;
import net.smiguel.app.util.DataUtils;

import java.util.List;

public class CustomerListAdapter
        extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder>
        implements EntityListAdapter<Customer> {

    private List<Customer> mCustomers;
    private View.OnClickListener mOnClickListener;
    private long customerIdToMark;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
        }
    }

    public CustomerListAdapter(List<Customer> customers) {
        this.mCustomers = customers;
    }

    public CustomerListAdapter(List<Customer> customers, View.OnClickListener onClickListener) {
        mCustomers = customers;
        mOnClickListener = onClickListener;
    }

    public CustomerListAdapter(List<Customer> customers, View.OnClickListener onClickListener, Long customerIdToMark) {
        mCustomers = customers;
        mOnClickListener = onClickListener;
        this.customerIdToMark = customerIdToMark;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_customer_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView txtName = holder.mView.findViewById(R.id.txt_name);
        TextView txtEmail = holder.mView.findViewById(R.id.txt_email);
        TextView txtBirthday = holder.mView.findViewById(R.id.txt_birthday);

        TextView txtStatusSync = holder.mView.findViewById(R.id.txt_status_sync);

        Customer customer = mCustomers.get(position);
        if (customer != null) {
            txtName.setText(customer.getName());
            txtEmail.setText(customer.getEmail());
            txtBirthday.setText(DataUtils.formatDate(customer.getBirthday()));

            txtStatusSync.setText(customer.isSyncPending() ? "Sync Pending" : "Sync");
            if (customer.isSyncPending()) {
                txtStatusSync.setTextColor(Color.RED);
            } else {
                txtStatusSync.setTextColor(Color.GREEN);
            }

            ConstraintLayout layout = holder.mView.findViewById(R.id.customerLayoutView);
            layout.setTag(position);

            if (mOnClickListener != null) {
                layout.setOnClickListener(v -> mOnClickListener.onClick(holder.mView));
            }

            if (customerIdToMark > 0 && customer.getId() == customerIdToMark) {
                layout.setBackgroundColor(Color.YELLOW);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    @Override
    public Customer getItemSelected(int position) {
        if (mCustomers.get(position) == null) {
            return null;
        }
        return mCustomers.get(position);
    }

    @Override
    public void removeAt(int position) {
        if (mCustomers.get(position) != null) {
            mCustomers.remove(position);
            notifyItemRemoved(position);
        }
    }
}
