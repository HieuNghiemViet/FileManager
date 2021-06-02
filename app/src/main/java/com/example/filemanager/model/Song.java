package com.example.filemanager.model;

public class Song {
    String nameSong;
    String path;
    long size;
    Long imageSong;
    String artistSong;
    long date;
    long duration;

    public Song(Long imageSong, String nameSong, String artistSong, String path, long size, long date, long duration) {
        this.imageSong = imageSong;
        this.nameSong = nameSong;
        this.artistSong = artistSong;
        this.path = path;
        this.size = size;
        this.date = date;
        this.duration = duration;
    }

    public Long getImageSong() {
        return imageSong;
    }

    public void setImageSong(Long imageSong) {
        this.imageSong = imageSong;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
