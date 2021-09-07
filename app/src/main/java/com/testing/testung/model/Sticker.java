package com.testing.testung.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Sticker implements Parcelable {
    public String imageFileName;
    public String imageFileNameLink;
    public List<String> emojis;
    public long size;

    public Sticker(String imageFileName,String imageFileNameLink, List<String> emojis) {
        this.imageFileName = imageFileName;
        this.imageFileNameLink = imageFileNameLink;
        this.emojis = emojis;
    }

    protected Sticker(Parcel in) {
        imageFileName = in.readString();
        imageFileNameLink = in.readString();
        emojis = in.createStringArrayList();
        size = in.readLong();
    }

    public static final Creator<Sticker> CREATOR = new Creator<Sticker>() {
        @Override
        public Sticker createFromParcel(Parcel in) {
            return new Sticker(in);
        }

        @Override
        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageFileName);
        dest.writeString(imageFileNameLink);
        dest.writeStringList(emojis);
        dest.writeLong(size);
    }
}
