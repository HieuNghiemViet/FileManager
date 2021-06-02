package com.example.filemanager.model;

public class Video {
    public String video;
    public String videoName;

    public Video(String video, String videoName) {
        this.video = video;
        this.videoName = videoName;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}
