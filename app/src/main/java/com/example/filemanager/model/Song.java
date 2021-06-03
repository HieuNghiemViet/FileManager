package com.example.filemanager.model;

public class Song {
    private String nameSong;
    private String artistSong;
    private String path;
    private long size;
    private Long imageSong;
    private long date;
    private long duration;
    private String displayName;

    public Song(Long imageSong, String nameSong, String artistSong, String path, long size, long date, long duration, String displayName) {
        this.imageSong = imageSong;
        this.nameSong = nameSong;
        this.artistSong = artistSong;
        this.path = path;
        this.size = size;
        this.date = date;
        this.duration = duration;
        this.displayName = displayName;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getArtistSong() {
        return artistSong;
    }

    public void setArtistSong(String artistSong) {
        this.artistSong = artistSong;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Long getImageSong() {
        return imageSong;
    }

    public void setImageSong(Long imageSong) {
        this.imageSong = imageSong;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
