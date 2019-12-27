package net.smiguel.app.view.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.smiguel.app.R;
import net.smiguel.app.view.fragment.AboutFragment;
import net.smiguel.app.view.fragment.BaseFragment;
import net.smiguel.app.view.fragment.CustomerListFragment;
import net.smiguel.app.view.fragment.HomeFragment;
import net.smiguel.app.view.handler.FragmentHandler;
import net.smiguel.app.view.handler.FragmentHandlerOperation;

public abstract class BaseDrawerActivity
        extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentHandlerOperation {

    private FragmentManager mFragmentManager;
    private FragmentHandler mFragmentHandler;
    private DrawerLayout mDrawerLayout;

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FragmentManager.OnBackStackChangedListener mBackStackChangedListener = () -> syncDrawerToggleState();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragmentHandler = new FragmentHandler(mFragmentManager);
        mFragmentManager.addOnBackStackChangedListener(mBackStackChangedListener);
    }

    private void syncDrawerToggleState() {
        ActionBarDrawerToggle toggle = this.mActionBarDrawerToggle;
        if (toggle != null) {
            if (mFragmentManager.getBackStackEntryCount() > 1) {
                toggle.setDrawerIndicatorEnabled(false);
                toggle.setToolbarNavigationClickListener(v -> mFragmentManager.popBackStack());

            } else {
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setToolbarNavigationClickListener(toggle.getToolbarNavigationClickListener());
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.view_drawer_frame, null);
        FrameLayout frameLayout = mDrawerLayout.findViewById(R.id.app_frame_layout);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        super.setContentView(mDrawerLayout);

        setupDrawer();
    }

    public void setDrawerUserData(String userName, String userLogin) {
        //IMPORTANT: This method needs to be adjusted according application requirements
        TextView txtUserName = getNavigationView().getHeaderView(0).findViewById(R.id.txt_user_name);
        TextView txtUserLogin = getNavigationView().getHeaderView(0).findViewById(R.id.txt_user_login);
        txtUserName.setText(userName);
        txtUserLogin.setText(userLogin);
    }

    private void setupDrawer() {
        mToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set arrow color
        Drawable upArrow = getApplicationContext().getDrawable(R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getNavigationView().setNavigationItemSelectedListener(this);
    }

    private NavigationView getNavigationView() {
        return findViewById(R.id.app_nav_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout != null) {
            mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {
                    syncDrawerToggleState();
                }

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {
                    syncDrawerToggleState();
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout == null) {
            super.onBackPressed();
        }

        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item != null ? item.getItemId() : R.id.nav_home;
        switch (id) {
            case R.id.nav_home:
                selectOptMenuHome();
                break;
            case R.id.nav_customer:
                selectOptMenuCustomer();
                break;
            case R.id.nav_about:
                selectOptMenuAbout();
                break;
            case R.id.nav_exit:
                showLogoutConfirmationMessage();
                break;
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentManager.removeOnBackStackChangedListener(mBackStackChangedListener);
        mFragmentManager = null;
    }

    //region Fragment Management
    @Override
    public void addFragment(BaseFragment fragment) {
        mFragmentHandler.addFragment(fragment);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack) {
        mFragmentHandler.addFragment(fragment, addToBackStack);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack, boolean validateCurrent) {
        mFragmentHandler.addFragment(fragment, addToBackStack, validateCurrent);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack, boolean validateCurrent, boolean clearBackStack) {
        mFragmentHandler.addFragment(fragment, addToBackStack, validateCurrent, clearBackStack);
    }

    @Override
    public void popUp() {
        mFragmentHandler.popUp();
    }

    @Override
    public void popUp(String name) {
        mFragmentHandler.popUp(name);
    }

    @Override
    public void popUpAll() {
        mFragmentHandler.popUpAll();
    }
    //endregion

    //region Menu options
    public void selectOptMenuHome() {
        addFragment(HomeFragment.newInstance(), true, true, true);
    }

    public void selectOptMenuCustomer() {
        addFragment(CustomerListFragment.newInstance());
    }

    public void selectOptMenuAbout() {
        addFragment(AboutFragment.newInstance(), true, true, false);
    }

    //endregion
}
