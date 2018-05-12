package com.anbetter.xplayer;

import android.app.Application;

import com.anbetter.log.MLog;
import com.facebook.fresco.helper.Phoenix;

/**
 * <p>
 * Created by android_ls on 2018/4/25.
 *
 * @author android_ls
 * @version 1.0
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Phoenix.init(this);
        MLog.init(true, "MLog");

    }

}
