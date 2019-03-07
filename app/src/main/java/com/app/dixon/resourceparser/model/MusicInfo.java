package com.app.dixon.resourceparser.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dixon.xu on 2019/3/5.
 * <p>
 * Music Info Model
 */

public class MusicInfo implements Parcelable {

    private int songId;
    private int albumId;
    private int duration;
    private String musicName;
    private String artist;
    private String filePath;
    private String folderPath;

    protected MusicInfo(Parcel in) {
        songId = in.readInt();
        albumId = in.readInt();
        duration = in.readInt();
        musicName = in.readString();
        artist = in.readString();
        filePath = in.readString();
        folderPath = in.readString();
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "songId=" + songId +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", musicName='" + musicName + '\'' +
                ", artist='" + artist + '\'' +
                ", filePath='" + filePath + '\'' +
                ", folderPath='" + folderPath + '\'' +
                '}';
    }

    public MusicInfo(int songId, int albumId, int duration, String musicName, String artist, String filePath, String folderPath) {
        this.songId = songId;
        this.albumId = albumId;
        this.duration = duration;
        this.musicName = musicName;
        this.artist = artist;
        this.filePath = filePath;
        this.folderPath = folderPath;
    }

    public MusicInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(songId);
        dest.writeInt(albumId);
        dest.writeInt(duration);
        dest.writeString(musicName);
        dest.writeString(artist);
        dest.writeString(filePath);
        dest.writeString(folderPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicInfo info = (MusicInfo) o;

        if (songId != info.songId) return false;
        return filePath != null ? filePath.equals(info.filePath) : info.filePath == null;
    }
}
