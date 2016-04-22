package com.laudev.android.splitpea;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kevin on 4/21/16.
 */
public class PersonAdapter extends BaseAdapter {
    private Context mContext;
    private int mResLayout;
    private List mData;
    private LayoutInflater mInflater;

    public PersonAdapter (Context context, int resLayout, List data) {
        mContext = context;
        mResLayout = resLayout;
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = newView(mContext, mData, parent);
        } else {
            view = convertView;
        }

        // bind view with new data

        return view;
    }

    public View newView(Context context, List data, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_item_person, parent, false);
        ListItemHolder holder = new ListItemHolder(view);
        view.setTag(holder);
        return view;
    }

    public void bindView(View view, Context context, List data) {
        ListItemHolder holder = (ListItemHolder)view.getTag();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

}
