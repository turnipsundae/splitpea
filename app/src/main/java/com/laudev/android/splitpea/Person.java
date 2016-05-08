package com.laudev.android.splitpea;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 4/21/16.
 */
public class Person implements Parcelable{
    private String mName;
    private List<Item> mItems = new ArrayList<>();
    private float mSubtotal;
    private float mTaxPercent;
    private float mTipPercent;
    private float mTotal;
    private int mCurrentItemPosition;

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
//        mItems = new ArrayList<Item>();
        mSubtotal = subtotal;
        mTaxPercent = taxPercent;
        mTipPercent = tipPercent;
        mTotal = getTotal();
        mCurrentItemPosition = 0;
    }

    public String getName() {
        return mName;
    }

    public float getSubtotal() {
        if (mItems != null) {
            mSubtotal = 0;
            for (Item item : mItems) {
                mSubtotal += item.getAmt();
            }
        }
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

    public List<Item> getItems() {
        return mItems;
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }

    public int getCurrentItemPosition() {
        return mCurrentItemPosition;
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
        mTaxPercent = tax / 100f;
    }

    public void setTipAmt(float tipAmt) {
        mTipPercent = tipAmt / mSubtotal;
    }

    public void setTipPercent(float tip) {
        mTipPercent = tip / 100f;
    }

    public void setItems(List items) {
        mItems = items;
    }

    public void setCurrentItemPosition(int position) {
        mCurrentItemPosition = position;
    }

    public void addItem(Item item) {
        mItems.add(item);
    }

    public void updateTotal() {
        mTotal = getSubtotal() + getTaxAmt() + getTipAmt();
    }

    public Person(Parcel parcel) {
        Log.v("Person", "reading parcel");
        mName = parcel.readString();
        mSubtotal = parcel.readFloat();
        mTaxPercent = parcel.readFloat();
        mTipPercent = parcel.readFloat();
        mTotal = parcel.readFloat();
        parcel.readTypedList(mItems, Item.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("Person", "writing parcel");
        dest.writeString(mName);
        dest.writeFloat(mSubtotal);
        dest.writeFloat(mTaxPercent);
        dest.writeFloat(mTipPercent);
        dest.writeFloat(mTotal);
        dest.writeTypedList(mItems);
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
