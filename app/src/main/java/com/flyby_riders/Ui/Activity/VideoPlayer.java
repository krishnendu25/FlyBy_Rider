package com.flyby_riders.Ui.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayer extends AppCompatActivity {


    String mVideo_url = "";
    @BindView(R.id.Video_Progrss)
    ProgressBar VideoProgrss;
    @BindView(R.id.video_layout)
    FrameLayout videoLayout;
    @BindView(R.id.videoView)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
        ButterKnife.bind(this);
        try {
            mVideo_url = getIntent().getStringExtra("mVideo_url");
        } catch (Exception e) {
            mVideo_url = "";
        }
        Instantiation();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer arg0) {
                VideoProgrss.setVisibility(View.GONE);
                videoView.start();
            }
        });

    }

    private void Instantiation() {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(mVideo_url);

    }
}