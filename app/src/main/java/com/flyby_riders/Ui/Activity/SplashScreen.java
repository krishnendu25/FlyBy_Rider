package com.flyby_riders.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.R;
import com.flyby_riders.Utils.Prefe;
import com.flyby_riders.Ui.Model.Contact_Model;
import com.flyby_riders.Ui.Model.FlyBy_Contact_Model;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.ic_splash)
    ImageView icSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        ButterKnife.bind(this);
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }
        clearLocalContact();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (new Prefe(getApplicationContext()).get_IsLogin().equalsIgnoreCase("true"))
                {
                    startActivity(new Intent(SplashScreen.this,DashBoard.class));
                    finish();
                }else
                {
                    if (new Prefe(getApplicationContext()).get_onBoarding().equalsIgnoreCase("true"))
                    {
                        startActivity(new Intent(SplashScreen.this, AppLandingView.class));
                        finish();
                    }else
                    {
                        startActivity(new Intent(SplashScreen.this,OnBoarding.class));
                        finish();
                    }
                }
            }
        };
        handler.postDelayed(runnable, 2100);

    }

    void clearLocalContact()
    {
        List<Contact_Model> local_contact = new ArrayList<>();
        List<FlyBy_Contact_Model> FlyBy_local_contact = new ArrayList<>();
        new Prefe(getApplicationContext()).setAllContact(local_contact);
        new Prefe(getApplicationContext()).setFlybyContact(FlyBy_local_contact);
    }

}

