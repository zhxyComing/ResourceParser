package com.app.dixon.resourceparser.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dixon.xu on 2019/3/5.
 * <p>
 * Music Info Model
 */

public class MusicAlbum implements Parcelable {

    private int id;
    private String name;
    private List<MusicInfo> infos;

    protected MusicAlbum(Parcel in) {
        id = in.readInt();
        name = in.readString();
        infos = in.createTypedArrayList(MusicInfo.CREATOR);
    }

    public MusicAlbum() {
    }

    public static final Creator<MusicAlbum> CREATOR = new Creator<MusicAlbum>() {
        @Override
        public MusicAlbum createFromParcel(Parcel in) {
            return new MusicAlbum(in);
        }

        @Override
        public MusicAlbum[] newArray(int size) {
            return new MusicAlbum[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MusicInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<MusicInfo> infos) {
        this.infos = infos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(infos);
    }

    @Override
    public String toString() {
        return "MusicAlbum{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", infos=" + infos +
                '}';
    }
}
