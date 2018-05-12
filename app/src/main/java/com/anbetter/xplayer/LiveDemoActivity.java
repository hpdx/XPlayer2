package com.anbetter.xplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.anbetter.xplayer.ijk.api.IXRenderView;
import com.anbetter.xplayer.ijk.api.IXVideoView;

/**
 * 视频播放器组件提供的基本功能示例Demo
 * 功能：
 * 1、支持播放直播流（rtmp）
 * 2、支持播放各种格式的多媒体（MP4、flv、MP3）
 * 3、支持多种视频画面显示方式：16:9、4:3、全屏、竖直居中等
 * 4、支持常用的动画(旋转、移动、缩放)
 * 5、支持倍速播放（加快或者减慢播放速度）
 * <p>
 * Created by android_ls on 2018/4/23.
 *
 * @author android_ls
 * @version 1.0
 */
public class LiveDemoActivity extends AppCompatActivity {

    private IXVideoView mVideoView;
    private int mRotation;
    private boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_demo);

//        String videoPath = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
        String videoPath = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
//        String videoPath = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
//        String videoPath = "http://jzvd.nathen.cn/1b61da23555d4ce28c805ea303711aa5/7a33ac2af276441bb4b9838f32d8d710-5287d2089db37e62345123a1be272f8b.mp4";


        mVideoView = findViewById(R.id.player_view);
        mVideoView.setDisplayAspectRatio(IXRenderView.AR_ASPECT_FIT_PARENT);
//        mVideoView.setDisplayAspectRatio(XIRenderView.AR_MATCH_PARENT);
        mVideoView.setVideoPath(videoPath);


        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.play();
            }
        });
        findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.pause();
            }
        });

        findViewById(R.id.btn_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.resume();
            }
        });

        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setDisplayAspectRatio(IXRenderView.AR_ASPECT_FILL_PARENT);
            }
        });
        findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setDisplayAspectRatio(IXRenderView.AR_ASPECT_FIT_PARENT);
            }
        });

        findViewById(R.id.btn_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setDisplayAspectRatio(IXRenderView.AR_4_3_FIT_PARENT);
            }
        });
        findViewById(R.id.btn_05).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setDisplayAspectRatio(IXRenderView.AR_16_9_FIT_PARENT);
            }
        });
        findViewById(R.id.btn_06).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setDisplayAspectRatio(IXRenderView.AR_MATCH_PARENT);
            }
        });

        findViewById(R.id.btn_speed_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setSpeed(0.25f);
            }
        });
        findViewById(R.id.btn_speed_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setSpeed(0.5f);
            }
        });
        findViewById(R.id.btn_speed_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setSpeed(1.0f);
            }
        });
        findViewById(R.id.btn_speed_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setSpeed(1.5f);
            }
        });
        findViewById(R.id.btn_speed_05).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setSpeed(2.0f);
            }
        });

        findViewById(R.id.btn_rotation_90).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRotation += 90;
                mVideoView.setVideoRotation(mRotation);

                if (mRotation == 360) {
                    mRotation = 0;
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

}
