package com.anbetter.xplayer.ijk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anbetter.xplayer.ijk.api.IXMediaPlayer;
import com.anbetter.xplayer.ijk.api.IXVideoView;
import com.anbetter.xplayer.ijk.listener.IXVideoViewListener;
import com.anbetter.xplayer.ijk.utils.XUtils;

/**
 * 视频播放器组件，支持在屏幕上任意拖动
 * <p>
 * Created by android_ls on 2018/4/26.
 *
 * @author android_ls
 * @version 1.0
 */
public class XDragVideoView extends FrameLayout implements IXVideoView {

    private boolean disableTrans;
    private int moveTouchSlop;
    private float lastTouchX;
    private float lastTouchY;
    private PointF dragTouchPoint;

    private XVideoView mVideoView;

    public XDragVideoView(@NonNull Context context) {
        super(context);
        setup(context);
    }

    public XDragVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    protected void setup(Context context) {
        // 这行必须添加，否则除了ACTION_DOWN外的其它手势事件将扑捉不到
        setClickable(true);

        moveTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mVideoView = new XVideoView(context);
        addView(mVideoView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        ImageView ivClose = new ImageView(context);
        ivClose.setImageResource(R.drawable.btn_small_close_selector);
        int padding = XUtils.dp2px(context, 3);
        ivClose.setPadding(padding, padding, padding, padding);
        ivClose.setLayoutParams(new LayoutParams(XUtils.dp2px(context, 30), XUtils.dp2px(context, 30)));
        addView(ivClose);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                setVisibility(View.GONE);
            }
        });
    }

    /**
     * 重置到初始化所在位置
     */
    public void restorePosition() {
        setTranslationX(0);
        setTranslationY(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float touchX = ev.getRawX();
        final float touchY = ev.getRawY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                disableTrans = false;
                lastTouchX = touchX;
                lastTouchY = touchY;
                dragTouchPoint = new PointF(touchX, touchY);
                return false;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(touchX - dragTouchPoint.x);
                float dy = Math.abs(touchY - dragTouchPoint.y);
                return dx >= moveTouchSlop || dy >= moveTouchSlop;
            default:
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        boolean consumed = false;
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (!disableTrans) {
                    float dx = x - lastTouchX;
                    float dy = y - lastTouchY;
                    float transX = getTranslationX() + dx;
                    float transY = getTranslationY() + dy;
                    setTranslationX(transX);
                    setTranslationY(transY);

                    lastTouchX = x;
                    lastTouchY = y;
                    consumed = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(event.getActionIndex()) == 0) {
                    disableTrans = true;
                }
                break;

            default:
        }
        return consumed || super.onTouchEvent(event);
    }

    @Override
    public void setVideoPath(@NonNull String path) {
        mVideoView.setVideoPath(path);
    }

    @Override
    public void setCoverView(@NonNull View view) {
        mVideoView.setCoverView(view);
    }

    @Override
    public void setMediaPlayer(IXMediaPlayer mediaPlayer) {
        mVideoView.setMediaPlayer(mediaPlayer);
    }

    @Override
    public void setDisplayAspectRatio(int displayAspectRatio) {
        mVideoView.setDisplayAspectRatio(displayAspectRatio);
    }

    @Override
    public void setVideoRotation(int degree) {
        mVideoView.setVideoRotation(degree);
    }

    @Override
    public void setLooping(boolean looping) {
        mVideoView.setLooping(looping);
    }

    @Override
    public int getCurrentState() {
        return mVideoView.getCurrentState();
    }

    @Override
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    @Override
    public int getDuration() {
        return mVideoView.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    @Override
    public void setNeedMute(boolean needMute) {
        mVideoView.setNeedMute(needMute);
    }

    @Override
    public void seekTo(long pos) {
        mVideoView.seekTo(pos);
    }

    @Override
    public void setSpeed(float speed) {
        mVideoView.setSpeed(speed);
    }

    @Override
    public void setVideoViewListener(IXVideoViewListener listener) {
        mVideoView.setVideoViewListener(listener);
    }

    @Override
    public Surface getSurface() {
        return mVideoView.getSurface();
    }

    @Override
    public void play() {
        mVideoView.play();
    }

    @Override
    public void pause() {
        mVideoView.pause();
    }

    @Override
    public void resume() {
        mVideoView.resume();
    }

    @Override
    public void stop() {
        mVideoView.stop();
    }

    @Override
    public void release() {
        mVideoView.release();
    }

}
