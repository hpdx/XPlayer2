package com.anbetter.xplayer.ijk.delegate;

import android.view.Surface;

import com.anbetter.log.MLog;
import com.anbetter.xplayer.ijk.XMediaPlayer;
import com.anbetter.xplayer.ijk.api.IXMediaPlayer;
import com.anbetter.xplayer.ijk.listener.XMediaPlayerListener;

/**
 * 提供了视频播放器类的单例对象，可供多个XIVideoView对象共享
 * <p>
 * Created by android_ls on 2018/4/27.
 *
 * @author android_ls
 * @version 1.0
 * @see IXMediaPlayer#setMediaPlayerListener(XMediaPlayerListener)
 */
public class XMediaPlayerDelegate implements IXMediaPlayer {

    private static XMediaPlayerDelegate sXMediaPlayerManager;
    private IXMediaPlayer mMediaPlayer;

    private XMediaPlayerDelegate() {
        mMediaPlayer = new XMediaPlayer();
        MLog.i("-----XMediaPlayerDelegate--------");
    }

    public static XMediaPlayerDelegate getInstance() {
        if (sXMediaPlayerManager == null) {
            synchronized (XMediaPlayerDelegate.class) {
                if (sXMediaPlayerManager == null) {
                    sXMediaPlayerManager = new XMediaPlayerDelegate();
                }
            }
        }
        return sXMediaPlayerManager;
    }

    public IXMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void setVideoPath(String path) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVideoPath(path);
        }
    }

    @Override
    public Surface getSurface() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getSurface();
        }
        return null;
    }

    @Override
    public void setSurface(Surface surface) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void releaseSurface() {
        if (mMediaPlayer != null) {
            mMediaPlayer.releaseSurface();
        }
    }

    @Override
    public void setLooping(boolean looping) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(looping);
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

    @Override
    public void setNeedMute(boolean needMute) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setNeedMute(needMute);
        }
    }

    @Override
    public void seekTo(long pos) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(pos);
        }
    }

    @Override
    public void setSpeed(float speed) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSpeed(speed);
        }
    }

    @Override
    public void setMediaPlayerListener(XMediaPlayerListener mediaPlayerListener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setMediaPlayerListener(mediaPlayerListener);
        }
    }

    @Override
    public void play() {
        if (mMediaPlayer != null) {
            mMediaPlayer.play();
        }
    }

    @Override
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.resume();
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    /**
     * 该方法在外部最好不要直接调用
     */
    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public int getVideoWidth() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoHeight();
        }
        return 0;
    }

    @Override
    public int getVideoSarNum() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoSarNum();
        }
        return 0;
    }

    @Override
    public int getVideoSarDen() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoSarDen();
        }
        return 0;
    }

    /**
     * 该方法推荐在Activity#onDestroy()方法中调用
     */
    public void destroy() {
        MLog.i("-----XMediaPlayerDelegate--destroy------");

        release();
        if (sXMediaPlayerManager != null) {
            sXMediaPlayerManager = null;
        }
    }

}
