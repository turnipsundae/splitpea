package com.laudev.android.splitpea;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * manages subtotal, tax, tip, total and remaining unaccounted amounts for one event
 */
public class Total implements Parcelable{
    private float mSubtotal;
    private float mTaxPercent;
    private float mTipPercent;
    private float mTotal;
    private float mSubtotalRemainder;

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
    public Total(float subtotal, float taxPercent, float tipPercent) {
        mSubtotal = subtotal;
        setTaxPercent(taxPercent);
        setTipPercent(tipPercent);
        mTotal = getTotal();
        mSubtotalRemainder = mTotal;
    }

    public float getSubtotal() {
        return mSubtotal;
    }

    public float getTaxAmt() {
        return mTaxPercent * mSubtotal;
    }

    public float getTaxPercent() {
        return mTaxPercent * 100f;
    }

    public float getTipAmt() {
        return mTipPercent * mSubtotal;
    }

    public float getTipPercent() {
        return mTipPercent * 100f;
    }

    public float getTotal() {
        return getSubtotal() + getTaxAmt() + getTipAmt();
    }

    public float getSubtotalRemainder() {
        return mSubtotalRemainder;
    }

    public void setSubtotal(float subtotal) {
        mSubtotal = subtotal;
    }

    public void setTaxAmt(float taxAmt) {
        mTaxPercent = taxAmt / mSubtotal;
    }

    public void setTaxPercent(float tax) {
        mTaxPercent = tax / 100f;
    }

    public void setTipAmt(float tipAmt) {
        mTipPercent = tipAmt / mSubtotal;
    }

    public void setTipPercent(float tip) {
        mTipPercent = tip / 100f ;
    }

    public void updateSubtotalRemainder(float amtPaid) {
        mSubtotalRemainder -= amtPaid;
    }

    public Total(Parcel parcel) {
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
        dest.writeFloat(mSubtotal);
        dest.writeFloat(mTaxPercent);
        dest.writeFloat(mTipPercent);
        dest.writeFloat(mTotal);
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
