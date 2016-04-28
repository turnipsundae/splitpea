package com.laudev.android.splitpea;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 4/21/16.
 */
public class Person implements Parcelable{
    private String mName;
    private float mSubtotal;
    private float mTax;
    private float mTip;
    private float mTotal;

    public Person() {
        this("Name", 0f, 0f, 0f);
    }

    public Person(String name) {
        this(name, 0f, 0f, 0f);
    }

    public Person(String name, float subtotal) {
        this(name, subtotal, 0f, 0f);
    }

    public Person(String name, float subtotal, float tax, float tip) {
        mName = name;
        mSubtotal = subtotal;
        mTax = tax;
        mTip = tip;
        updateTotal();
    }

    public String getName() {
        return mName;
    }

    public float getSubtotal() {
        return mSubtotal;
    }

    public float getTax() {
        return mTax;
    }

    public float getTip() {
        return mTip;
    }

    public float getTotal() {
        return mTotal;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setSubtotal(float subtotal) {
        mSubtotal = subtotal;
    }

    public void setTax(float tax) {
        mTax = tax;
    }

    public void setTip (float tip) {
        mTip = tip;
    }

    public void updateTotal() {
        mTotal = mSubtotal * ( 1 + mTax ) * ( 1 + mTip );
    }

    public Person(Parcel parcel) {
        mName = parcel.readString();
        mSubtotal = parcel.readFloat();
        mTax = parcel.readFloat();
        mTip = parcel.readFloat();
        mTotal = parcel.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeFloat(mSubtotal);
        dest.writeFloat(mTax);
        dest.writeFloat(mTip);
        dest.writeFloat(mTotal);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel parcel) {
            return new Person(parcel);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
