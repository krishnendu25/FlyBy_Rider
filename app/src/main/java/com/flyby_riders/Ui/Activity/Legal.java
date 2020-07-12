package com.flyby_riders.Ui.Activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Legal extends BaseActivity {

    @BindView(R.id.BACK_BTN)
    ImageView BACKBTN;
    @BindView(R.id.ACTIVITY_TITEL)
    TextView ACTIVITYTITEL;
    @BindView(R.id.webview_Legal)
    WebView webviewLegal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.BACK_BTN)
    public void onViewClicked() {
        finish();
    }
}
