package net.smiguel.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.smiguel.app.R;
import net.smiguel.app.view.adapter.OnBoardingPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnBoardingActivity extends BaseActivity {

    public static final int STEPS = 3;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.linear_circles)
    LinearLayout mLinearLayoutCircles;

    @BindView(R.id.btn_skip)
    Button btnSkip;

    @BindView(R.id.btn_done)
    Button btnDone;

    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        mUnbinder = ButterKnife.bind(this);
        setViewEvents();
        setViewPager();

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void setViewEvents() {
        btnSkip.setOnClickListener(v -> finishOnboarding());
        btnNext.setOnClickListener(v -> stepForwardOnboarding());
        btnDone.setOnClickListener(v -> finishOnboarding());
    }

    private void setViewPager() {
        mViewPager.setAdapter(new OnBoardingPageAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                btnSkip.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                btnDone.setVisibility(View.GONE);
                mLinearLayoutCircles.setVisibility(View.GONE);

                switch (position) {
                    case 0:
                        btnNext.setVisibility(View.VISIBLE);
                        btnSkip.setVisibility(View.VISIBLE);
                        mLinearLayoutCircles.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        btnNext.setVisibility(View.VISIBLE);
                        btnSkip.setVisibility(View.VISIBLE);
                        mLinearLayoutCircles.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        btnDone.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        buildCircles();
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            finish();
        } else {
            stepBackOnboarding();
        }
    }

    private void stepBackOnboarding() {
        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    private void stepForwardOnboarding() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
    }

    private void buildCircles() {
        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for (int index = 0; index < STEPS; index++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.ic_circle_white);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            mLinearLayoutCircles.addView(circle);
        }
        setIndicator(0);
    }

    private void setIndicator(int circleSelectedIndex) {
        if (circleSelectedIndex < STEPS) {
            for (int circleIndex = 0; circleIndex < STEPS; circleIndex++) {
                ImageView circle = (ImageView) mLinearLayoutCircles.getChildAt(circleIndex);
                if (circleIndex == circleSelectedIndex) {
                    circle.setColorFilter(getResources().getColor(R.color.text_selected));
                } else {
                    circle.setColorFilter(getResources().getColor(R.color.background_white));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void finishOnboarding() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
