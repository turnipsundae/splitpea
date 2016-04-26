package com.laudev.android.splitpea;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * manages subtotal, tax, tip, total and remaining unaccounted amounts for one event
 */
public class Total implements Parcelable{
    private float mRemainder;
    private float mSubtotal;
    private float mTax;
    private float mTip;
    private float mTotal;


    public Total() {
        this(0f, 0f, 0f);
    }

    public Total(float subtotal) {
        this(subtotal, 0f, 0f);
    }

    public Total(float subtotal, float tax) {
        this(subtotal, tax, 0f);
    }

    /*
    * @param subtotal
    */
    public Total(float subtotal, float tax, float tip) {
        mSubtotal = subtotal;
        mTax = tax;
        mTip = tip;
        updateTotal();
        mRemainder = mTotal;
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

    public float getRemainder() {
        return mRemainder;
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

    public void updateRemainder(float amtPaid) {
        mRemainder -= amtPaid;
    }

    public void updateTotal() {
        mTotal = mSubtotal * ( 1 + mTax ) * ( 1 + mTip );
    }

    public Total(Parcel parcel) {
        mSubtotal = parcel.readFloat();
        mTax = parcel.readFloat();
        mTip = parcel.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mSubtotal);
        dest.writeFloat(mTax);
        dest.writeFloat(mTip);
    }

    public static final Parcelable.Creator<Total> CREATOR = new Parcelable.Creator<Total>() {
        public Total createFromParcel(Parcel parcel) {
            return new Total(parcel);
        }

        public Total[] newArray(int size) {
            return new Total[size];
        }
    };
}
