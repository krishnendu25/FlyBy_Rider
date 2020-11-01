package com.flyby_riders.Ui.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.flyby_riders.R;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayer extends AppCompatActivity {


    String mVideo_url = "";
    @BindView(R.id.videoView)
    UniversalVideoView videoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mediaController;
    @BindView(R.id.Video_Progrss)
    ProgressBar VideoProgrss;
    @BindView(R.id.video_layout)
    FrameLayout videoLayout;

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


    }

    private void Instantiation() {
        Uri uri = Uri.parse(mVideo_url);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setMediaController(mediaController);
        videoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {

            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {

            }
        });
    }
}