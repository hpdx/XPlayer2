package com.anbetter.xplayer.ijk;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anbetter.log.MLog;
import com.anbetter.xplayer.ijk.api.IXMediaPlayer;
import com.anbetter.xplayer.ijk.api.IXRenderView;
import com.anbetter.xplayer.ijk.api.IXVideoView;
import com.anbetter.xplayer.ijk.delegate.XMediaPlayerDelegate;
import com.anbetter.xplayer.ijk.listener.IXVideoViewListener;
import com.anbetter.xplayer.ijk.listener.XMediaPlayerListener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 视频播放器组件
 * 备注：
 * 1、在列表中多个视频切换播放，目前采用共享同一个XIMediaPlayer对象
 * 2、当在列表中使用该组件（多个）时，需要调用setMediaPlayer(XIMediaPlayer mediaPlayer)方法
 * <p>
 * Created by android_ls on 2018/4/19.
 *
 * @author android_ls
 * @version 1.0
 * @see #setMediaPlayer(IXMediaPlayer mediaPlayer)
 * @see XMediaPlayerDelegate#getInstance()
 */
public class XVideoView extends FrameLayout implements IXVideoView,
        XMediaPlayerListener, IXRenderView.OnSurfaceStatusListener, AudioManager.OnAudioFocusChangeListener {

    public static final String TAG = XVideoView.class.getSimpleName();

    protected View mCoverView;
    protected IXRenderView mRenderView;
    protected IXMediaPlayer mMediaPlayer;
    protected AudioManager mAudioManager;
    private XVideoControls mVideoControls;
    private IXVideoViewListener mVideoViewListener;

    protected String mVideoPath;
    protected boolean mLooping;
    protected int currentFocus = 0;

    protected int mVideoWidth;
    protected int mVideoHeight;
    protected int mVideoSarNum;
    protected int mVideoSarDen;

    public XVideoView(@NonNull Context context) {
        super(context);
        setup(context);
    }

    public XVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public XVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context);
    }

    protected void setup(Context context) {
        mAudioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        mRenderView = new TextureRenderView(context);
        mRenderView.setOnSurfaceStatusListener(this);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        addView(mRenderView.getView(), lp);

//        mVideoControls = new XVideoControls(context);
//        setControls(mVideoControls);
    }

    @Override
    public void setCoverView(@NonNull View view) {
        mCoverView = view;
    }

    @Override
    public void setMediaPlayer(IXMediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }

    public void setVideoRotation(int degree) {
        if (mRenderView != null) {
            if (mRenderView instanceof TextureRenderView) {
                mRenderView.setVideoRotation(degree);
            }
        }
    }

    @Override
    public void setDisplayAspectRatio(int displayAspectRatio) {
        if (mRenderView != null) {
            mRenderView.setAspectRatio(displayAspectRatio);
        }
    }

    @Override
    public void setVideoPath(@NonNull String path) {
        mVideoPath = path;
    }

    @Override
    public void setLooping(boolean looping) {
        mLooping = looping;
    }

    @Override
    public void seekTo(long pos) {
        if (isPlaying()) {
            mMediaPlayer.seekTo(pos);
        }
    }

    @Override
    public void setSpeed(float speed) {
        if (isPlaying()) {
            mMediaPlayer.setSpeed(speed);
        }
    }

    @Override
    public void setVideoViewListener(IXVideoViewListener listener) {
        mVideoViewListener = listener;
    }

    @Override
    public Surface getSurface() {
        if (mRenderView != null) {
            return mRenderView.getSurface();
        }
        return null;
    }

    @Override
    public void setNeedMute(boolean needMute) {
        if (isPlaying()) {
            mMediaPlayer.setNeedMute(needMute);
        }
    }

    @Override
    public int getCurrentState() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentState();
        }
        return 0;
    }

    @Override
    public void play() {
        if (mMediaPlayer == null) {
            MLog.i("play() mMediaPlayer == null");
            mMediaPlayer = new XMediaPlayer();
        }

        int currentState = mMediaPlayer.getCurrentState();
        MLog.i("play() currentState = " + currentState);
        if (currentState == mMediaPlayer.STATE_IDLE
                || currentState == mMediaPlayer.STATE_ERROR) {
            boolean focusAudioRequest = requestAudioFocus();
            MLog.i("play() focusAudioRequest = " + focusAudioRequest);
            if (!focusAudioRequest) {
                return;
            }

            mMediaPlayer.reset();
            setKeepScreenOn(true);

            if (mRenderView != null && mRenderView.getSurface() != null) {
                MLog.i("play()-->setSurface()");
                mMediaPlayer.setSurface(mRenderView.getSurface());
            }

            mMediaPlayer.setMediaPlayerListener(this);
            mMediaPlayer.setVideoPath(mVideoPath);
            mMediaPlayer.setLooping(mLooping);
            mMediaPlayer.play();
        } else if (currentState == mMediaPlayer.STATE_PAUSED) {
            mMediaPlayer.resume();
        } else if (currentState == mMediaPlayer.STATE_COMPLETED) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }

        setKeepScreenOn(false);
        abandonAudioFocus();
    }

    @Override
    public void resume() {
        boolean focusAudioRequest = requestAudioFocus();
        MLog.i("resume() focusAudioRequest = " + focusAudioRequest);
        if (!focusAudioRequest) {
            return;
        }

        setKeepScreenOn(true);
        if (mMediaPlayer != null) {
            mMediaPlayer.resume();
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        setKeepScreenOn(false);
        abandonAudioFocus();
    }

    @Override
    public void release() {
        stop();

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public int getVideoSarNum() {
        return mVideoSarNum;
    }

    public int getVideoSarDen() {
        return mVideoSarDen;
    }

    @Override
    public void onSurfaceCreated(Surface surface, int width, int height) {
        MLog.i("onSurfaceCreated");
        if (mMediaPlayer != null) {
            MLog.i("onSurfaceCreated()-->setSurface()");
            mMediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void onSurfaceSizeChanged(Surface surface, int width, int height) {
        MLog.i("onSurfaceChanged");

    }

    @Override
    public void onSurfaceDestroyed(Surface surface) {
        MLog.i("onSurfaceDestroyed");
        if (mMediaPlayer != null) {
            MLog.i("onSurfaceDestroyed-->releaseSurface()");
            mMediaPlayer.releaseSurface();
        }
    }

    @Override
    public void onPrepared(IXMediaPlayer mp) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onError(int what, int extra) {

    }

    @Override
    public void onInfo(int what, int extra) {
        if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            // 视频准备渲染
            showCoverView(false);
        }
    }

    @Override
    public void onVideoSizeChanged(IXMediaPlayer mp, int width, int height, int sarNum, int sarDe) {
//        MLog.i("=====================onVideoSizeChanged======================");

        mVideoWidth = mp.getVideoWidth();
        mVideoHeight = mp.getVideoHeight();
        mVideoSarNum = mp.getVideoSarNum();
        mVideoSarDen = mp.getVideoSarDen();

//        MLog.i("---->mVideoWidth =" + mVideoWidth);
//        MLog.i("---->mVideoHeight =" + mVideoHeight);
//        MLog.i("---->mVideoSarNum =" + mVideoSarNum);
//        MLog.i("---->mVideoSarDen =" + mVideoSarDen);

        if (mVideoWidth != 0 && mVideoHeight != 0) {
            if (mRenderView != null) {
                mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
                mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
            }
            requestLayout();
        }
    }

    @Override
    public void onCompletion() {
        MLog.i("onCompletion");

    }

    private void showCoverView(boolean toVisible) {
        if (mCoverView != null) {
            mCoverView.setVisibility(toVisible ? View.VISIBLE : View.GONE);
        }
    }

    protected boolean requestAudioFocus() {
        if (currentFocus == AudioManager.AUDIOFOCUS_GAIN) {
            return true;
        }

        if (mAudioManager == null) {
            return false;
        }

        int focusRequestGranted = mAudioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        MLog.i("focusRequestGranted = " + focusRequestGranted);

//        AudioManager.AUDIOFOCUS_REQUEST_FAILED
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == focusRequestGranted) {
            currentFocus = AudioManager.AUDIOFOCUS_GAIN;
            return true;
        }
        return false;
    }

    protected void abandonAudioFocus() {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(null);
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
//        MLog.i("focusChange = " + focusChange);
        if (currentFocus == focusChange) {
            return;
        }

        currentFocus = focusChange;
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                // 获得了Audio Focus
                if (!isPlaying()) {
                    play();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // 失去了Audio Focus，并将会持续很长的时间。
                // 这里因为可能会停掉很长时间，所以不仅仅要停止Audio的播放，最好直接释放掉Media资源。
                if (isPlaying()) {
                    stop();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // 暂时失去Audio Focus，并会很快再次获得。
                // 必须停止Audio的播放，但是因为可能会很快再次获得AudioFocus，这里可以不释放Media资源；
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // 暂时失去AudioFocus，但是可以继续播放，不过要在降低音量。
                if (isPlaying()) {
                    stop();
                }
                break;
        }
    }

    public void setControls(@Nullable XVideoControls controls) {
        if (mVideoControls != null && mVideoControls != controls) {
            removeView(mVideoControls);
        }

        if (controls != null) {
            mVideoControls = controls;
            controls.setVideoView(this);
            addView(controls, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        MLog.i("--->onAttachedToWindow");

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        MLog.i("--->onDetachedFromWindow");

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
//        MLog.i("=================finalize=================");

    }

}
