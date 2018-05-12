package com.anbetter.xplayer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anbetter.log.MLog;
import com.anbetter.xplayer.ijk.api.IXRenderView;
import com.anbetter.xplayer.ijk.api.IXVideoView;
import com.anbetter.xplayer.ijk.delegate.XMediaPlayerDelegate;
import com.anbetter.xplayer.model.VideoInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;

import java.util.ArrayList;

/**
 * 视频列表数据适配器
 *
 * @author android_ls
 * @version 1.0
 * <p>
 * Created by android_ls on 2018/4/23.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoPlayerHolder> {

    private ArrayList<VideoInfo> mList;

    public VideoListAdapter(ArrayList<VideoInfo> data) {
        mList = data;
    }

    @NonNull
    @Override
    public VideoPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoPlayerHolder(View.inflate(parent.getContext(), R.layout.live_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoPlayerHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewRecycled(@NonNull VideoPlayerHolder holder) {
        MLog.i("---------onViewRecycled-----------");
        holder.stop();
    }

    public static class VideoPlayerHolder extends RecyclerView.ViewHolder {

        IXVideoView mVideoView;
        SimpleDraweeView sdvCoverView;
        TextView tvVideoDesc;

        public VideoPlayerHolder(View itemView) {
            super(itemView);
            mVideoView = itemView.findViewById(R.id.video_view);
            sdvCoverView = itemView.findViewById(R.id.sdv_cover_view);
            tvVideoDesc = itemView.findViewById(R.id.tv_video_desc);

            // 设置音视频播放器， 目的为了让列表共享同一个XIMediaPlayer对象
            mVideoView.setMediaPlayer(XMediaPlayerDelegate.getInstance());
        }

        public void bind(final VideoInfo videoInfo) {
            mVideoView.setVideoPath(videoInfo.videoUrl);
            mVideoView.setLooping(false);
            mVideoView.setDisplayAspectRatio(IXRenderView.AR_MATCH_PARENT);
            mVideoView.setCoverView(sdvCoverView);

            tvVideoDesc.setText("视频格式：" + videoInfo.desc);
            Phoenix.with(sdvCoverView).load(videoInfo.coverUrl);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), LiveRoomActivity.class);
                    intent.putExtra("url", videoInfo.videoUrl);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void play() {
            mVideoView.play();
        }

        public void stop() {
            mVideoView.stop();
        }

    }

}
