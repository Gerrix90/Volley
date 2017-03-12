package com.aaa.gerrix.volleycache.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class)
public class Lost extends BaseModel implements Parcelable{



    @Column
    @PrimaryKey
    int id;

    @Column
    String title;

    @Column
    Blob image;

    public Lost() {
    }

    protected Lost(Parcel in) {
        id = in.readInt();
        title = in.readString();
    }

    public static final Creator<Lost> CREATOR = new Creator<Lost>() {
        @Override
        public Lost createFromParcel(Parcel in) {
            return new Lost(in);
        }

        @Override
        public Lost[] newArray(int size) {
            return new Lost[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
    }
}
