package com.anbetter.xplayer.ijk;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.anbetter.log.MLog;
import com.anbetter.xplayer.ijk.api.IXRenderView;
import com.anbetter.xplayer.ijk.utils.ViewMeasureUtils;

/**
 * 视频画面渲染视图，继承自TextureView
 * 支持View的常用动画效果，不会有黑边、黑屏产生，可以在列表中使用
 * <p>
 * Created by android_ls on 2018/4/19.
 *
 * @author android_ls
 * @version 1.0
 */
public class TextureRenderView extends TextureView implements TextureView.SurfaceTextureListener, IXRenderView {

    private ViewMeasureUtils mMeasureHelper;
    private Surface mSurface;
    private OnSurfaceStatusListener mOnSurfaceStatusListener;

    public TextureRenderView(Context context) {
        super(context);
        setup(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context);
    }

    private void setup(Context context) {
        mMeasureHelper = new ViewMeasureUtils(this);
        setSurfaceTextureListener(this);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    @Override
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    @Override
    public void setVideoRotation(int degree) {
        mMeasureHelper.setVideoRotation(degree);
        setRotation(degree);
    }

    @Override
    public void setAspectRatio(int aspectRatio) {
        mMeasureHelper.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureHelper.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
    }

    @Override
    public Surface getSurface() {
        return mSurface;
    }

    @Override
    public void setOnSurfaceStatusListener(OnSurfaceStatusListener onSurfaceStatusListener) {
        mOnSurfaceStatusListener = onSurfaceStatusListener;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        // 在SurfaceTexture准备使用时调用。
        MLog.i("================onSurfaceTextureAvailable");

        mSurface = new Surface(surface);
        if (mOnSurfaceStatusListener != null) {
            MLog.i("=========22=======onSurfaceTextureAvailable");
            mOnSurfaceStatusListener.onSurfaceCreated(mSurface, width, height);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // 当SurfaceTexture缓冲区大小更改时调用。
        MLog.i("================onSurfaceTextureSizeChanged");

        if (mOnSurfaceStatusListener != null) {
            MLog.i("========22========onSurfaceTextureSizeChanged");
            mOnSurfaceStatusListener.onSurfaceSizeChanged(mSurface, width, height);
        }
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        MLog.i("================onSurfaceTextureUpdated");
        // 当指定SurfaceTexture的更新时调用updateTexImage()。
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        // 当指定SurfaceTexture即将被销毁时调用。如果返回true，则调用此方法后，表面纹理中不会发生渲染。
        // 如果返回false，则客户端需要调用release()。大多数应用程序应该返回true。
        MLog.i("================onSurfaceTextureDestroyed");

        if (mOnSurfaceStatusListener != null) {
            MLog.i("=========22=======onSurfaceTextureDestroyed");
            mOnSurfaceStatusListener.onSurfaceDestroyed(mSurface);
        }
        return true;
    }

}
