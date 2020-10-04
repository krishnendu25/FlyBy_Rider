package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Prefe;
import com.flyby_riders.Utils.BaseActivity;
import com.flyby_riders.Utils.OnSwipeTouchListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnBoarding extends BaseActivity {


    @BindView(R.id.Background_bg)
    ImageView BackgroundBg;
    @BindView(R.id.Background_Color)
    RelativeLayout BackgroundColor;
    @BindView(R.id.Title_tv)
    TextView TitleTv;
    @BindView(R.id.Subtitle_tv)
    TextView SubtitleTv;
    @BindView(R.id.next_tv)
    TextView nextTv;
    @BindView(R.id.bottom_view)
    RelativeLayout bottomView;
    @BindView(R.id.pageInd1)
    ImageView pageInd1;
    @BindView(R.id.pageInd2)
    ImageView pageInd2;
    @BindView(R.id.pageInd3)
    ImageView pageInd3;
    @BindView(R.id.pageInd4)
    ImageView pageInd4;
    private List<String> Title = Arrays.asList("Your \n" +
            "Garage", "Stock is good, Upgrades\nmake it personal", "Only the best &\nhandpicked.", "Rides for \n" +
            "true riders");
    private List<String> SubTitle = Arrays.asList("Add and manage all your bikes and documents in one place",
            "Find Upgrades, Parts, Gears and offer specific to your Bike Model.",
            "Garages, repair shops, workshops and other essential bike services near you",
            "Create and manage Rides & trips with your fellow riders or just for yourself");
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        ButterKnife.bind(this);
        Instantiation();
        View_Update(0);

        BackgroundColor.setOnTouchListener(new OnSwipeTouchListener(OnBoarding.this) {
            public void onSwipeTop() {

            }

            public void onSwipeRight() {
                RightSwap();
            }

            public void onSwipeLeft() {
                LeftSwap();
            }

            public void onSwipeBottom() {
            }

        });

    }

    private void Instantiation() {

    }

    private void View_Update(int i) {
        try {
            indicatorController(i);
            TitleTv.setText(Title.get(i));
            SubtitleTv.setText(SubTitle.get(i));
            if (i == 0) {
                nextTv.setText("NEXT");
                BackgroundBg.setImageDrawable(getResources().getDrawable(R.drawable.frf1));
            } else if (i == 1) {
                nextTv.setText("NEXT");
                BackgroundBg.setImageDrawable(getResources().getDrawable(R.drawable.frf2));
            } else if (i == 2) {
                nextTv.setText("NEXT");
                BackgroundBg.setImageDrawable(getResources().getDrawable(R.drawable.frf3));

            } else if (i == 3) {
                nextTv.setText("SIGN IN");
                BackgroundBg.setImageDrawable(getResources().getDrawable(R.drawable.frf4));
            }


        } catch (NullPointerException e) {

        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {

        }

    }

    @OnClick(R.id.next_tv)
    public void onViewClicked() {
        LeftSwap();
    }

    void LeftSwap() {
        if (count == 0) {
            View_Update(1);
            count++;
            return;
        } else if (count == 1) {
            View_Update(2);
            count++;
            return;
        } else if (count == 2) {
            View_Update(3);
            count++;
            return;
        }
        if (count == 3 && nextTv.getText().toString().trim().equalsIgnoreCase("SIGN IN")) {
            startActivity(new Intent(this, AppLandingView.class));
            new Prefe(this).set_onBoarding("true");
            finish();
        }
    }

    void RightSwap() {
        if (count == 1) {
            View_Update(0);
            count--;
            return;
        } else if (count == 2) {
            View_Update(1);
            count--;
            return;
        } else if (count == 3) {
            View_Update(2);
            count--;
            return;
        }
    }


    void indicatorController(int pos)
    {
        switch (pos)
        {
            case 0:
                pageInd1.setImageDrawable(getResources().getDrawable(R.drawable.selected_dot));
                pageInd2.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd3.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd4.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));


                break;
            case 1:
                pageInd1.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd2.setImageDrawable(getResources().getDrawable(R.drawable.selected_dot));
                pageInd3.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd4.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                break;
            case 2:
                pageInd1.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd2.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd3.setImageDrawable(getResources().getDrawable(R.drawable.selected_dot));
                pageInd4.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                break;
            case 3:
                pageInd1.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd2.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd3.setImageDrawable(getResources().getDrawable(R.drawable.default_dot));
                pageInd4.setImageDrawable(getResources().getDrawable(R.drawable.selected_dot));
                break;
        }
    }
}
