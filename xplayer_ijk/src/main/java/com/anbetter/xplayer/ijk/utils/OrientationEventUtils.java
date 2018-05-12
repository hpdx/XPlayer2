package com.anbetter.xplayer.ijk.utils;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.view.OrientationEventListener;

/**
 * 手机屏幕方向管理器
 * <p>
 * Created by android_ls on 2018/4/23.
 *
 * @author android_ls
 * @version 1.0
 */
public class OrientationEventUtils extends OrientationEventListener {

    private Context mContext;
    private OnScreenOrientationListener mScreenOrientationListener;
    private boolean mClick;

    // 0-竖屏，1-横屏，2-反向横屏
    private int mOrientationType;
    // 当前屏幕方向（当前手机竖着，但是屏幕为横屏）
    private int mCurrentScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public OrientationEventUtils(Context context, OnScreenOrientationListener listener) {
        super(context);
        this.mContext = context;
        this.mScreenOrientationListener = listener;
    }

    @Override
    public void onOrientationChanged(int rotation) {
        // 在系统设置里，设置的是否锁定屏幕方向，返回true表示根据手机旋转方向而切换横竖屏
        boolean autoRotateOn = (Settings.System.getInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
        if (!autoRotateOn) {
            return;
        }

        if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
            if (mClick) {
                if (mOrientationType > 0) {
                    return;
                }

                mClick = false;
                mOrientationType = 0;
            } else {
                if (mOrientationType > 0) {
                    // 切换为竖屏
                    mCurrentScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mOrientationType = 0;
                    mClick = false;

                    if (mScreenOrientationListener != null) {
                        mScreenOrientationListener.onScreenOrientationChange(mCurrentScreenOrientation);
                    }
                }
            }
        } else if (((rotation >= 230) && (rotation <= 310))) {
            if (mClick) {
                if (mOrientationType != 1) {
                    return;
                }

                mClick = false;
                mOrientationType = 1;
            } else {
                if (!(mOrientationType == 1)) {
                    // 设置为横屏
                    mCurrentScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mOrientationType = 1;
                    mClick = false;

                    if (mScreenOrientationListener != null) {
                        mScreenOrientationListener.onScreenOrientationChange(mCurrentScreenOrientation);
                    }
                }
            }
        } else if (rotation > 30 && rotation < 95) {
            if (mClick) {
                if (mOrientationType != 2) {
                    return;
                }

                mClick = false;
                mOrientationType = 2;
            } else if (mOrientationType != 2) {
                // 设置为反向横屏
                mCurrentScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                mOrientationType = 2;
                mClick = false;

                if (mScreenOrientationListener != null) {
                    mScreenOrientationListener.onScreenOrientationChange(mCurrentScreenOrientation);
                }
            }
        }
    }

    /**
     * 横竖屏切换，从竖屏-->横屏，从横屏-->竖屏
     */
    public void switchScreenOrientation() {
        mClick = true;
        if (mOrientationType == 0) {
            mCurrentScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mOrientationType = 1;
        } else {
            mCurrentScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mOrientationType = 0;
        }

        if (mScreenOrientationListener != null) {
            mScreenOrientationListener.onScreenOrientationChange(mCurrentScreenOrientation);
        }
    }

    public interface OnScreenOrientationListener {
        /**
         * 获取屏幕的当前方向
         *
         * @param orientation ActivityInfo.SCREEN_ORIENTATION_PORTRAIT、ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
         */
        void onScreenOrientationChange(int orientation);
    }

}
