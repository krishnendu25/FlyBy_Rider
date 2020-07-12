package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;
import com.flyby_riders.Ui.Adapter.Onbroading_adapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnBoarding extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.Backward_btn)
    ImageView BackwardBtn;
    @BindView(R.id.dots_indicator)
    DotsIndicator dotsIndicator;
    @BindView(R.id.Forward_btn)
    ImageView ForwardBtn;
    @BindView(R.id.GOTO_SIGNIN)
    TextView GOTOSIGNIN;
    @BindView(R.id.ProgressBar)
    ProgressBar ProgressBar;
    int PAGER_NUMBER;
    CountDownTimer mCountDownTimer;
    int i=0;
    Onbroading_adapter onbroading_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        ButterKnife.bind(this);
        Instantiation();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PAGER_NUMBER=position;
            }

            @Override
            public void onPageSelected(int position) {
                Set_Progress();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void Instantiation() {
        PAGER_NUMBER=0;
        onbroading_adapter = new Onbroading_adapter(getSupportFragmentManager());
        viewPager.setAdapter(onbroading_adapter);
        dotsIndicator.setViewPager(viewPager);
        Set_Progress();
        viewPager.setCurrentItem(0,true);

    }

    @OnClick({R.id.Backward_btn, R.id.Forward_btn, R.id.GOTO_SIGNIN})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Backward_btn:
                PAGER_SLIDER(false);
                break;
            case R.id.Forward_btn:
                PAGER_SLIDER(true);
                break;
            case R.id.GOTO_SIGNIN:
                startActivity(new Intent(this,Choose_Way_Screen.class));
                new Session(this).set_onBoarding("true");
                break;
        }
    }

    private void PAGER_SLIDER(boolean b)
    {
        ProgressBar.setProgress(0);
        if (b)
        {
            if (PAGER_NUMBER==0)
            {
                viewPager.setCurrentItem(1,true);
                GOTOSIGNIN.setVisibility(View.GONE);
                ForwardBtn.setVisibility(View.VISIBLE);
            }else if (PAGER_NUMBER==1)
            {
                viewPager.setCurrentItem(2,true);
                GOTOSIGNIN.setVisibility(View.GONE);
                ForwardBtn.setVisibility(View.VISIBLE);
            }else if (PAGER_NUMBER==2)
            {
                viewPager.setCurrentItem(3,true);
                GOTOSIGNIN.setVisibility(View.GONE);
                ForwardBtn.setVisibility(View.VISIBLE);

            }else if (PAGER_NUMBER==3)
            {
                GOTOSIGNIN.setVisibility(View.VISIBLE);
                ForwardBtn.setVisibility(View.GONE);
            }

        }else
        {
            if (PAGER_NUMBER==0)
            {
                GOTOSIGNIN.setVisibility(View.GONE);
                ForwardBtn.setVisibility(View.VISIBLE);
            }else if (PAGER_NUMBER==1)
            {
                GOTOSIGNIN.setVisibility(View.GONE);
                ForwardBtn.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(0,true);
            }else if (PAGER_NUMBER==2)
            {
                GOTOSIGNIN.setVisibility(View.GONE);
                ForwardBtn.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(1,true);
            }else if (PAGER_NUMBER==3)
            {
                viewPager.setCurrentItem(2,true);
                GOTOSIGNIN.setVisibility(View.VISIBLE);
                ForwardBtn.setVisibility(View.GONE);
            }
        }

    }

    void Set_Progress()
    {
        ProgressBar.setProgress(i);
        mCountDownTimer=new CountDownTimer(4000,500) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                ProgressBar.setProgress((int)i*100/(4000/500));

            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                ProgressBar.setProgress(100);
                PAGER_SLIDER(true);
                i=0;
            }
        };
        mCountDownTimer.start();
    }
}
