package com.anbetter.xplayer.ijk.listener;
import com.anbetter.xplayer.ijk.api.IXMediaPlayer;

/**
 * 视频播放器，播放状态监听器
 * <p>
 * Created by android_ls on 2018/4/24.
 *
 * @author android_ls
 * @version 1.0
 */
public interface XMediaPlayerListener {

    void onPrepared(IXMediaPlayer mp);

    void onBufferingUpdate(int percent);

    void onSeekComplete();

    void onError(int what, int extra);

    void onInfo(int what, int extra);

    void onVideoSizeChanged(IXMediaPlayer mp, int width, int height, int sarNum, int sarDe);

    void onCompletion();

}
