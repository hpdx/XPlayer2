package com.anbetter.xplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.anbetter.xplayer.ijk.api.IXRenderView;
import com.anbetter.xplayer.ijk.api.IXVideoView;

/**
 * 直播间示例Demo
 * <p>
 * Created by android_ls on 2018/4/24.
 *
 * @author android_ls
 * @version 1.0
 */
public class LiveRoomActivity extends AppCompatActivity {

    private IXVideoView mVideoView;
    private float scaleRatio = 1.0F; // 缩放比例
    private ObjectAnimator scaleAnimator;
    private boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live_room);

        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra("url");
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }

        mVideoView = findViewById(R.id.video_view);
        mVideoView.setVideoPath(videoUrl);
        mVideoView.setLooping(false);
        mVideoView.setDisplayAspectRatio(IXRenderView.AR_ASPECT_FIT_PARENT);
        mVideoView.play();

        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scaleRatio == 1.0f) {
                    scale(0.5f);
                } else {
                    scale(1.0f);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
            isPause = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isPause) {
            return;
        }

        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mVideoView != null) {
            mVideoView.release();
        }
        super.onDestroy();
    }

    private void scale(float ratio) {
        if (scaleRatio == ratio) {
            return;
        }

        if (scaleAnimator != null) {
            scaleAnimator.cancel();
        }

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", scaleRatio, ratio);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", scaleRatio, ratio);
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", 0);
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", 0);
        scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(mVideoView, scaleX, scaleY, translationX, translationY);
        scaleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animation.removeAllListeners();
                scaleAnimator = null;
            }
        });
        scaleAnimator.setDuration(150L).start();
        scaleRatio = ratio;
    }

}
