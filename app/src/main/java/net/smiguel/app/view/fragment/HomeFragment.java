package net.smiguel.app.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.smiguel.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.lnk_list_customer)
    LinearLayout mLinearLayoutListCustomer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mLinearLayoutListCustomer.setOnClickListener(v -> addFragment(CustomerListFragment.newInstance()));

        setTitle(getString(R.string.home_title));
        return view;
    }
}
