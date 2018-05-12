package com.anbetter.xplayer.ijk.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * 工具类
 * <p>
 * Created by android_ls on 2018/4/26.
 *
 * @author android_ls
 * @version 1.0
 */
public class XUtils {

    public static int getDisplayWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeight(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dip) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dip,
                r.getDisplayMetrics());
        return (int) px;
    }

}
