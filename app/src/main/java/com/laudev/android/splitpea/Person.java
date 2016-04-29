package com.laudev.android.splitpea;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 4/21/16.
 */
public class Person implements Parcelable{
    private String mName;
    private float mSubtotal;
    private float mTaxPercent;
    private float mTipPercent;
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

    public Person(String name, float subtotal, float taxPercent, float tipPercent) {
        mName = name;
        mSubtotal = subtotal;
        mTaxPercent = taxPercent;
        mTipPercent = tipPercent;
        updateTotal();
    }

    public String getName() {
        return mName;
    }

    public float getSubtotal() {
        return mSubtotal;
    }

    public float getTaxAmt() {
        return mTaxPercent * mSubtotal;
    }

    public float getTaxPercent() {
        return mTaxPercent;
    }

    public float getTipAmt() {
        return mTipPercent * mSubtotal;
    }

    public float getTipPercent() {
        return mTipPercent;
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

    public void setTaxAmt(float taxAmt) {
        mTaxPercent = taxAmt / mSubtotal;
    }

    public void setTaxPercent(float tax) {
        mTaxPercent = tax;
    }

    public void setTipAmt(float tipAmt) {
        mTipPercent = tipAmt / mSubtotal;
    }

    public void setTipPercent(float tip) {
        mTipPercent = tip;
    }

    public void updateTotal() {
        mTotal = mSubtotal + getTaxAmt() + getTipAmt();
    }

    public Person(Parcel parcel) {
        mName = parcel.readString();
        mSubtotal = parcel.readFloat();
        mTaxPercent = parcel.readFloat();
        mTipPercent = parcel.readFloat();
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
        dest.writeFloat(mTaxPercent);
        dest.writeFloat(mTipPercent);
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
