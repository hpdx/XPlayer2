package com.anbetter.xplayer.model;

/**
 * 视频信息实体类
 * <p>
 * Created by android_ls on 2018/4/25.
 *
 * @author android_ls
 * @version 1.0
 */
public class VideoInfo {

    public String videoUrl;
    public String coverUrl;
    public String desc;

    public VideoInfo() {
    }

    public VideoInfo(String videoUrl, String coverUrl) {
        this.videoUrl = videoUrl;
        this.coverUrl = coverUrl;
    }

}
