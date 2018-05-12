package com.anbetter.xplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.anbetter.log.MLog;
import com.anbetter.xplayer.discretescrollview.DSVOrientation;
import com.anbetter.xplayer.discretescrollview.DiscreteScrollView;
import com.anbetter.xplayer.discretescrollview.transform.ScaleTransformer;
import com.anbetter.xplayer.ijk.delegate.XMediaPlayerDelegate;
import com.anbetter.xplayer.model.VideoInfo;

import java.util.ArrayList;

/**
 * 直播列表示例Demo
 * 实现思路：
 * 1、列表中所有XIVideoView对象共享同一个XIMediaPlayer播放器对象
 * 2、从之前正在播放的卡片到新的将要播放的卡片，XIMediaPlayer播放器对象做的动作依次是stop、reset、
 * setVideoPath、setSurface、play
 * 3、XIMediaPlayer播放器对象创建非常消耗资源，所以推荐尽可能的共享
 * <p>
 * Created by android_ls on 2018/4/23.
 *
 * @author android_ls
 * @version 1.0
 */
public class LiveListActivity extends AppCompatActivity
        implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {

    String[] imageUrls = {
            "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
            "http://ww1.sinaimg.cn/large/610dc034ly1fjaxhky81vj20u00u0ta1.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fivohbbwlqj20u011idmx.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fj78mpyvubj20u011idjg.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fj3w0emfcbj20u011iabm.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fiz4ar9pq8j20u010xtbk.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fis7dvesn6j20u00u0jt4.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fir1jbpod5j20ip0newh3.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fik2q1k3noj20u00u07wh.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fiednrydq8j20u011itfz.jpg"
    };
    String[] videoUrls = {
            "http://down.fodizi.com/05/d4267-11.flv",
            "rtmp://live.hkstv.hk.lxdns.com/live/hks",
            "http://jzvd.nathen.cn/1b61da23555d4ce28c805ea303711aa5/7a33ac2af276441bb4b9838f32d8d710-5287d2089db37e62345123a1be272f8b.mp4",
            "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4",
            "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4",
            "http://mp4.vjshi.com/2016-12-22/e54d476ad49891bd1adda49280a20692.mp4"
    };

    private DiscreteScrollView mDsvLiveList;
    private ArrayList<VideoInfo> mList;
    private VideoListAdapter mAdapter;

    private int centerPos = -1;
    private boolean isPause;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        mHandler = new Handler(Looper.getMainLooper());

        mList = new ArrayList<>();
        for (int i = 0; i < videoUrls.length; i++) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.coverUrl = imageUrls[i];
            String videoUrl = videoUrls[i];
            videoInfo.videoUrl = videoUrl;

            if (videoUrl.startsWith("rtmp://")) {
                videoInfo.desc = "rtmp";
            } else {
                videoInfo.desc = videoUrl.substring(videoUrl.lastIndexOf("."));
            }

            mList.add(videoInfo);
        }

        mDsvLiveList = findViewById(R.id.dsv_live_list);
        mDsvLiveList.setOrientation(DSVOrientation.HORIZONTAL);
        mDsvLiveList.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.80f)
                .build());

        mDsvLiveList.setItemTransitionTimeMillis(150);
        mDsvLiveList.addOnItemChangedListener(this);
        mDsvLiveList.setHasFixedSize(true);

        mAdapter = new VideoListAdapter(mList);
        mDsvLiveList.setAdapter(mAdapter);
        mDsvLiveList.setOffscreenItems(mList.size());

//        mDsvLiveList.setSlideOnFlingThreshold(3000);
//        mDsvLiveList.setClampTransformProgressAfter(n);
//        mDsvLiveList.setSlideOnFling(true);

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        if (centerPos != adapterPosition) {
            if (centerPos != -1) {
                RecyclerView.ViewHolder videoHolder = mDsvLiveList.findViewHolderForAdapterPosition(centerPos);
                if (videoHolder != null && videoHolder instanceof VideoListAdapter.VideoPlayerHolder) {
                    MLog.i("---------stop-----------");
                    ((VideoListAdapter.VideoPlayerHolder) videoHolder).stop();
                }
            }

            RecyclerView.ViewHolder videoHolder = mDsvLiveList.findViewHolderForAdapterPosition(mDsvLiveList.getCurrentItem());
            if (videoHolder != null && videoHolder instanceof VideoListAdapter.VideoPlayerHolder) {
                MLog.i("---------play-----------");
                ((VideoListAdapter.VideoPlayerHolder) videoHolder).play();
            }

            centerPos = adapterPosition;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (centerPos != -1) {
            RecyclerView.ViewHolder videoHolder = mDsvLiveList.findViewHolderForAdapterPosition(centerPos);
            if (videoHolder != null && videoHolder instanceof VideoListAdapter.VideoPlayerHolder) {
                MLog.i("-----onPause----stop-----------");
                ((VideoListAdapter.VideoPlayerHolder) videoHolder).stop();
                isPause = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isPause) {
            return;
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (centerPos == -1) {
                    return;
                }

                RecyclerView.ViewHolder videoHolder = mDsvLiveList.findViewHolderForAdapterPosition(centerPos);
                if (videoHolder != null && videoHolder instanceof VideoListAdapter.VideoPlayerHolder) {
                    MLog.i("-----onResume----play-----------");
                    ((VideoListAdapter.VideoPlayerHolder) videoHolder).play();
                    isPause = false;
                }
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        MLog.i("-----onDestroy------release--------");
        XMediaPlayerDelegate.getInstance().destroy();
        super.onDestroy();
    }

}
