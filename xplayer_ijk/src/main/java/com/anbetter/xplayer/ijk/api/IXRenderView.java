package com.anbetter.xplayer.ijk.api;

import android.view.Surface;
import android.view.View;

/**
 * 视频画面视图（视频画面渲染视图）
 * <p>
 * Created by android_ls on 2018/4/19.
 *
 * @author android_ls
 * @version 1.0
 */
public interface IXRenderView {

    /**
     * 水平方向拉伸填满屏幕，竖直方向居中
     */
    int AR_ASPECT_FIT_PARENT = 0;

    /**
     * 拉伸到填充满指定尺寸，对视频画面居中裁剪
     */
    int AR_ASPECT_FILL_PARENT = 1;

    /**
     * 保持视频内容的原始尺寸
     */
    int AR_ASPECT_WRAP_CONTENT = 2;

    /**
     * 拉伸到填充满指定尺寸
     */
    int AR_MATCH_PARENT = 3;

    /**
     * 宽高比16:9，宽是填充满指定的尺寸的
     */
    int AR_16_9_FIT_PARENT = 4;

    /**
     * 宽高比4:3，宽是填充满指定的尺寸的
     */
    int AR_4_3_FIT_PARENT = 5;

    View getView();

    Surface getSurface();

    void setOnSurfaceStatusListener(OnSurfaceStatusListener onSurfaceStatusListener);

    void setAspectRatio(int aspectRatio);

    void setVideoSize(int videoWidth, int videoHeight);

    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    /**
     * 只针对TextureRenderView组件起作用
     *
     * @param degree
     */
    void setVideoRotation(int degree);

    interface OnSurfaceStatusListener {
        void onSurfaceCreated(Surface surface, int width, int height);

        void onSurfaceSizeChanged(Surface surface, int width, int height);

        void onSurfaceDestroyed(Surface surface);
    }

}
