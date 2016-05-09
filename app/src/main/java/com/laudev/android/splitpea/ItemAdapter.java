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
    private List mData;
    private LayoutInflater mInflater;

    public ItemAdapter (Context context, int resLayout, List data) {
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
        bindView(view, mContext, (Item)mData.get(position), position);

        return view;
    }

    public View newView(Context context, List data, ViewGroup parent) {
        View view = mInflater.inflate(mResLayout, parent, false);
        ListItemDetailHolder holder = new ListItemDetailHolder(view);
        view.setTag(holder);
        return view;
    }

    public void bindView(View view, Context context, Item item, int position) {
        ListItemDetailHolder holder = (ListItemDetailHolder)view.getTag();
        holder.mItem.setText(item.getName());
        holder.mAmt.setText(context.getString(R.string.format_amount, item.getAmt()));
        holder.mButton.setOnClickListener(new RemoveItemClick(position));
    }

    public class RemoveItemClick implements View.OnClickListener {
        private int position;
        public RemoveItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            mData.remove(position);
            notifyDataSetChanged();
        }
    }

    public void add(Object object) {
        if (object instanceof Item) {
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
