package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.R;
import com.flyby_riders.Sharedpreferences.Session;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash_Screen extends AppCompatActivity {

    @BindView(R.id.ic_splash)
    ImageView icSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        ButterKnife.bind(this);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (new Session(getApplicationContext()).get_IsLogin().equalsIgnoreCase("true"))
                {
                    startActivity(new Intent(Splash_Screen.this,DashBoard.class));
                    finish();
                }else
                {
                    if (new Session(getApplicationContext()).get_onBoarding().equalsIgnoreCase("true"))
                    {
                        startActivity(new Intent(Splash_Screen.this,Choose_Way_Screen.class));
                        finish();
                    }else
                    {
                        startActivity(new Intent(Splash_Screen.this,OnBoarding.class));
                        finish();
                    }
                }
            }
        };
        handler.postDelayed(runnable, 2100);

    }



}

