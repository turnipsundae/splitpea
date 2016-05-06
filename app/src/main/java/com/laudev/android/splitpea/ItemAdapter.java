package com.laudev.android.splitpea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by kevin on 5/6/16.
 */
public class ItemAdapter extends BaseAdapter {
    private Context mContext;
    private int mResLayout;
    private float[] mData;
    private LayoutInflater mInflater;
    private ItemAdapter mItemAdapter;

    public ItemAdapter (Context context, int resLayout, float[] data) {
        mContext = context;
        mResLayout = resLayout;
        mData = data.clone();
        mInflater = LayoutInflater.from(context);
        mItemAdapter = this;
    }

    @Override
    public int getCount() {
        return mData.length;
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
        bindView(view, mContext, mData[position]);

        return view;
    }

    public View newView(Context context, float[] data, ViewGroup parent) {
        View view = mInflater.inflate(mResLayout, parent, false);
        ListItemDetailHolder holder = new ListItemDetailHolder(view);
        view.setTag(holder);
        return view;
    }

    public void bindView(View view, Context context, float amount) {
        ListItemDetailHolder holder = (ListItemDetailHolder)view.getTag();
        holder.mItem.setText(context.getString(R.string.item));
        holder.mAmt.setText(context.getString(R.string.format_dollar_amount, amount));
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemAdapter.add(0f);
            }
        });
    }

    public void add(float item) {
        float[] newItems = new float[mData.length + 1];
        System.arraycopy(mData, 0, newItems, 0, mData.length);
        newItems[newItems.length - 1] = item;
        mData = newItems;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }
}
