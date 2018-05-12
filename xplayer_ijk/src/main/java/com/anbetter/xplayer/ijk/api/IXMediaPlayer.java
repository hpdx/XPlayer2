package com.anbetter.xplayer.ijk.api;

import android.view.Surface;
import com.anbetter.xplayer.ijk.listener.XMediaPlayerListener;

/**
 * 视频播放器
 * <p>
 * Created by android_ls on 2018/4/18.
 *
 * @author android_ls
 * @version 1.0
 */
public interface IXMediaPlayer {

    /**
     * 出错了
     */
    int STATE_ERROR = -1;

    /**
     * 空闲状态、闲置的
     */
    int STATE_IDLE = 0;

    /**
     * 准备中
     */
    int STATE_PREPARING = 1;

    /**
     * 准备好了
     */
    int STATE_PREPARED = 2;

    /**
     * 播放中
     */
    int STATE_PLAYING = 3;

    /**
     * 暂停
     */
    int STATE_PAUSED = 4;

    /**
     * 播放结束
     */
    int STATE_COMPLETED = 5;

    /**
     * 设置播放地址
     *
     * @param path
     */
    void setVideoPath(String path);

    /**
     * 获取Surface对象
     */
    Surface getSurface();

    /**
     * 设置Surface
     *
     * @param surface
     */
    void setSurface(Surface surface);

    /**
     * 释放Surface对象
     */
    void releaseSurface();

    /**
     * 是否循环播放
     *
     * @param looping
     */
    void setLooping(boolean looping);

    /**
     * 播放器当前处所状态
     *
     * @return 其返回值有以下几个：
     * @see #STATE_ERROR
     * @see #STATE_IDLE
     * @see #STATE_PREPARING
     * @see #STATE_PREPARED
     * @see #STATE_PLAYING
     * @see #STATE_PAUSED
     * @see #STATE_COMPLETED
     */
    int getCurrentState();

    /**
     * 是否正在播放
     *
     * @return true表示正在播放
     */
    boolean isPlaying();

    /**
     * 获取播放文件总时间
     *
     * @return
     */
    int getDuration();

    /**
     * 获取当前播放的进度值（当前播放到哪儿了）
     *
     * @return 当前播放位置
     */
    int getCurrentPosition();

    /**
     * 设置是否静音
     *
     * @param needMute true表示开启静音模式
     */
    void setNeedMute(boolean needMute);

    /**
     * 设置当前的播放进度
     *
     * @param pos
     */
    void seekTo(long pos);

    /**
     * 设置视频的播放速度
     *
     * @param speed 默认值1.0
     */
    void setSpeed(float speed);

    /**
     * 设置视频播放器，在播放过程中的事件回调监听器
     *
     * @param mediaPlayerListener XMediaPlayerListener
     */
    void setMediaPlayerListener(XMediaPlayerListener mediaPlayerListener);

    void play();

    void start();

    void pause();

    void resume();

    void stop();

    void reset();

    /**
     * 该方法推荐在XIVideoView#release()方法中调用
     */
    void release();

    int getVideoWidth();

    int getVideoHeight();

    int getVideoSarNum();

    int getVideoSarDen();

}
