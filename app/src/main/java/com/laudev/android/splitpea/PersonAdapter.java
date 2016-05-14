package com.laudev.android.splitpea;

import android.content.Context;
import android.util.Log;
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
    private int mDisplayMode;

    public static final int SUBTOTAL_ONLY = 0;
    public static final int SUBTOTAL_AND_TAX = 1;
    public static final int SUBTOTAL_AND_TAX_PLUS_TIP = 2;
    public static final int TOTAL_ONLY = 3;

    public PersonAdapter (Context context, int resLayout, List data, int displayMode) {
        mContext = context;
        mResLayout = resLayout;
        mData = data;
        mInflater = LayoutInflater.from(context);
        mDisplayMode = displayMode;
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
        Log.v("PersonAdapter", "BindView called");
        ListItemHolder holder = (ListItemHolder)view.getTag();
        holder.mName.setText(person.getName());
        switch (mDisplayMode) {
            case SUBTOTAL_ONLY:
                holder.mAmt.setText(context.getString(R.string.format_dollar_amount, person.getSubtotal()));
                break;
            case SUBTOTAL_AND_TAX:
                holder.mAmt.setText(context.getString(R.string.format_dollar_amount, person.getSubtotal() + person.getTaxAmt()));
                break;
            case SUBTOTAL_AND_TAX_PLUS_TIP:
                holder.mAmt.setText(context.getString(R.string.format_share_total_tip,
                        person.getSubtotal() + person.getTaxAmt(),
                        person.getTipAmt()));
                break;
            case TOTAL_ONLY:
                holder.mAmt.setText(context.getString(R.string.format_dollar_amount, person.getTotal()));
                break;
        }
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

    public void setDisplayMode(int displayMode) {
        mDisplayMode = displayMode;
    }

    public int getDisplayMode() {
        return mDisplayMode;
    }
}
