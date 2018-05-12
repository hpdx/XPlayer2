package com.anbetter.xplayer.ijk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.anbetter.xplayer.ijk.api.IXVideoView;

/**
 * <p>
 * Created by android_ls on 2018/5/8.
 *
 * @author android_ls
 * @version 1.0
 */
public class XVideoControls extends FrameLayout {

    private IXVideoView mVideoView;
    private ImageView iv_start;

    public XVideoControls(Context context) {
        super(context);
        setup(context);
    }

    public XVideoControls(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    protected void setup(Context context) {
        LayoutInflater.from(context).inflate(R.layout.video_view_controls_layout, this);
        iv_start = findViewById(R.id.iv_start);
        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoView == null) {
                    return;
                }

                if(mVideoView.isPlaying()) {
                    iv_start.setImageResource(R.drawable.jz_click_pause_selector);
                    mVideoView.pause();
                } else {
                    iv_start.setImageResource(R.drawable.jz_click_play_selector);
                    mVideoView.play();
                }
            }
        });
    }

    public void setVideoView(IXVideoView videoView) {
        this.mVideoView = videoView;
    }

    public void show() {

    }

    public void hide() {

    }

}
