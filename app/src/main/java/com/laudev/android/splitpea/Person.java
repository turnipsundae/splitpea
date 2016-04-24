package com.laudev.android.splitpea;

/**
 * Created by kevin on 4/21/16.
 */
public class Person {
    private String mName;
    private float mSubtotal;
    private float mTip;

    public Person() {
        this("Name", 0f, 0f);
    }

    public Person(String name) {
        this(name, 0f, 0f);
    }

    public Person(String name, float subtotal) {
        this(name, subtotal, 0f);
    }

    public Person(String name, float subtotal, float tip) {
        mName = name;
        mSubtotal = subtotal;
        mTip = tip;
    }

    public String getName() {
        return mName;
    }

    public float getSubtotal() {
        return mSubtotal;
    }

    public float getTip() {
        return mTip;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setSubtotal(float subtotal) {
        mSubtotal = subtotal;
    }

    public void setTip (float tip) {
        mTip = tip;
    }
}
