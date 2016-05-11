package com.laudev.android.splitpea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        bindView(view, mContext, (Person)mData.get(position));

        return view;
    }

    public View newView(Context context, List data, ViewGroup parent) {
        View view = mInflater.inflate(mResLayout, parent, false);
        ListItemHolder holder = new ListItemHolder(view);
        view.setTag(holder);
        return view;
    }

    public void bindView(View view, Context context, Person person) {
        ListItemHolder holder = (ListItemHolder)view.getTag();
        holder.mName.setText(person.getName());
        holder.mAmt.setText(context.getString(R.string.format_dollar_amount, person.getSubtotal() + person.getTaxAmt()));
    }

    public void add(Object object) {
        if (object instanceof Person) {
            mData.add(object);
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

}
