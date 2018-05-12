package com.anbetter.xplayer.ijk.api;

import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.View;
import com.anbetter.xplayer.ijk.listener.IXVideoViewListener;

/**
 * 视频播放器组件
 * <p>
 * Created by android_ls on 2018/4/23.
 *
 * @author android_ls
 * @version 1.0
 */
public interface IXVideoView {

    /**
     * 设置播放地址
     *
     * @param path
     */
    void setVideoPath(@NonNull String path);

    /**
     * 设置视频未播放前的预览画面（视频封面）
     *
     * @param view
     */
    void setCoverView(@NonNull View view);

    /**
     * 设置音视频播放器
     * <p>
     * 备注：
     * 1、每一个XIVideoView对象默认内置一个XIMediaPlayer对象
     * 2、当且仅当想让多个XIVideoView对象共用同一个XIMediaPlayer对象时，调用该方法进行设置
     * 3、若不需要XIMediaPlayer对象进行共享的话，调用或者不调用这个方法，效果一致。
     *
     * @param mediaPlayer XIMediaPlayer
     */
    void setMediaPlayer(IXMediaPlayer mediaPlayer);

    /**
     * 设置显示的比例
     */
    void setDisplayAspectRatio(int displayAspectRatio);

    /**
     * 只针对TextureRenderView组件起作用
     *
     * @param degree
     */
    void setVideoRotation(int degree);

    /**
     * 是否循环播放
     *
     * @param looping
     */
    void setLooping(boolean looping);

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
     * 设置Video当前状态监听器
     * @param listener
     */
    void setVideoViewListener(IXVideoViewListener listener);

    /**
     * 获取当前XIVideoView所持有的Surface对象
     *
     * @return
     */
    Surface getSurface();

    /**
     * 播放器当前处所状态
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

    void play();

    void pause();

    void resume();

    void stop();

    /**
     * 该方法推荐在Activity#onDestroy()方法中调用
     */
    void release();

}
