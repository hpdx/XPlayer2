package com.anbetter.xplayer;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.anbetter.xplayer.ijk.XDragVideoView;
import com.anbetter.xplayer.ijk.api.IXMediaPlayer;
import com.anbetter.xplayer.ijk.api.IXRenderView;
import com.anbetter.xplayer.ijk.api.IXVideoView;
import com.anbetter.xplayer.ijk.delegate.XMediaPlayerDelegate;

/**
 * 大小视频窗口切换Demo
 * 实现思路：
 * 1、采用多个XIVideoView共享同一个XIMediaPlayer播放器对象
 * 2、在滚动结束后，为XIMediaPlayer对象设置当前显示XIVideoView所持有的Surface
 * 3、XIMediaPlayer播放器播放视频的动作从未停止，只是切换展示视频画面的Surface而已，从而实现无缝切换大小窗口的功能
 * 4、小窗口支持在屏幕任意位置拖动，在拖动过程中播放动作不中断。
 * <p>
 * Created by android_ls on 2018/4/26.
 *
 * @author android_ls
 * @version 1.0
 */
public class ScrollingActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final int APP_BAR_LAYOUT_IDLE = 0;
    public static final int APP_BAR_LAYOUT_EXPANDED = 1;
    public static final int APP_BAR_LAYOUT_COLLAPSED = 2;

    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private boolean isPause;
    private String videoUrl;
    private int mAppBarLayoutCurrentState = APP_BAR_LAYOUT_IDLE;

    private IXMediaPlayer mMediaPlayer;
    private IXVideoView mVideoView;
    private XDragVideoView mSmallVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitle("XPlayer");

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(this);

        videoUrl = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";

        mSmallVideoView = findViewById(R.id.drag_video_view);
        mVideoView = findViewById(R.id.video_view);

        mMediaPlayer = XMediaPlayerDelegate.getInstance();

        mSmallVideoView.setMediaPlayer(mMediaPlayer);
        mSmallVideoView.setDisplayAspectRatio(IXRenderView.AR_MATCH_PARENT);

        mVideoView.setMediaPlayer(mMediaPlayer);
        mVideoView.setDisplayAspectRatio(IXRenderView.AR_MATCH_PARENT);

        mMediaPlayer.setVideoPath(videoUrl);
        mMediaPlayer.setLooping(false);
        mMediaPlayer.play();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        MLog.i("verticalOffset = " + verticalOffset);
        if (verticalOffset == 0) {
            if (mAppBarLayoutCurrentState != APP_BAR_LAYOUT_EXPANDED) {
                mCollapsingToolbarLayout.setTitle("");

                if (mVideoView.getSurface() != null) {
                    mMediaPlayer.setSurface(mVideoView.getSurface());
                }

                if (mSmallVideoView.getVisibility() == View.VISIBLE) {
                    mSmallVideoView.setVisibility(View.GONE);
                }

                mAppBarLayoutCurrentState = APP_BAR_LAYOUT_EXPANDED;
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mAppBarLayoutCurrentState != APP_BAR_LAYOUT_COLLAPSED) {
                mCollapsingToolbarLayout.setTitle("大小视频显示组件互动");

                if (mSmallVideoView.getVisibility() == View.GONE) {
                    mSmallVideoView.setVisibility(View.VISIBLE);
                    // 重置到初始化所在位置
                    mSmallVideoView.restorePosition();

                    if (mSmallVideoView.getSurface() != null) {
                        mMediaPlayer.setSurface(mSmallVideoView.getSurface());
                    }
                }

                mAppBarLayoutCurrentState = APP_BAR_LAYOUT_COLLAPSED;
            }
        } else {
            if (mAppBarLayoutCurrentState != APP_BAR_LAYOUT_IDLE) {
                if (mAppBarLayoutCurrentState == APP_BAR_LAYOUT_COLLAPSED) {
                    // 中间状态
                    mSmallVideoView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mVideoView.getSurface() != null) {
                                mMediaPlayer.setSurface(mVideoView.getSurface());
                            }

                            if (mSmallVideoView.getVisibility() == View.VISIBLE) {
                                mSmallVideoView.setVisibility(View.GONE);
                            }
                        }
                    }, 50);
                }
                mAppBarLayoutCurrentState = APP_BAR_LAYOUT_IDLE;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isPause) {
            return;
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        super.onDestroy();
    }

}
