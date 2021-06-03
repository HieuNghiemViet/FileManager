package com.example.filemanager.model;

public class Video {
    private String nameVideo;
    private String pathVideo;
    private long sizeVideo;
    private long durationVideo;
    private long dateVideo;
    private String displayName;

    public Video(String nameVideo, String pathVideo, long sizeVideo, long durationVideo, long dateVideo, String displayName) {
        this.nameVideo = nameVideo;
        this.pathVideo = pathVideo;
        this.sizeVideo = sizeVideo;
        this.durationVideo = durationVideo;
        this.dateVideo = dateVideo;
        this.displayName = displayName;
    }

    public String getNameVideo() {
        return nameVideo;
    }

    public void setNameVideo(String nameVideo) {
        this.nameVideo = nameVideo;
    }

    public String getPathVideo() {
        return pathVideo;
    }

    public void setPathVideo(String pathVideo) {
        this.pathVideo = pathVideo;
    }

    public long getSizeVideo() {
        return sizeVideo;
    }

    public void setSizeVideo(long sizeVideo) {
        this.sizeVideo = sizeVideo;
    }

    public long getDurationVideo() {
        return durationVideo;
    }

    public void setDurationVideo(long durationVideo) {
        this.durationVideo = durationVideo;
    }

    public long getDateVideo() {
        return dateVideo;
    }

    public void setDateVideo(long dateVideo) {
        this.dateVideo = dateVideo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
