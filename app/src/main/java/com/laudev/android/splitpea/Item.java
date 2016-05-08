package com.laudev.android.splitpea;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 5/7/16.
 */
public class Item implements Parcelable{
    private String mName;
    private float mAmt;

    public Item(String name, float amt) {
        mName = name;
        mAmt = amt;
    }

    public String getName() {
        return mName;
    }

    public float getAmt() {
        return mAmt;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAmt(float amt) {
        mAmt = amt;
    }

    public Item(Parcel parcel) {
        mName = parcel.readString();
        mAmt = parcel.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeFloat(mAmt);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel parcel) {
            return new Item(parcel);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
