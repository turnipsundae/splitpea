package com.laudev.android.splitpea;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    private Person mPerson;
    private LayoutInflater mInflater;
    private ItemAdapter mItemAdapter;

    public ItemAdapter (Context context, int resLayout, Person person) {
        mContext = context;
        mResLayout = resLayout;
        mPerson = person;
        mInflater = LayoutInflater.from(context);
        mItemAdapter = this;
    }

    @Override
    public int getCount() {
        return mPerson.getItems().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = newView(mContext, mPerson.getItems(), parent);
        } else {
            view = convertView;
        }

        // bind view with new data
        bindView(view, mContext, mPerson.getItem(position), position);

        return view;
    }

    public View newView(Context context, float[] data, ViewGroup parent) {
        View view = mInflater.inflate(mResLayout, parent, false);
        ListItemDetailHolder holder = new ListItemDetailHolder(view);
        view.setTag(holder);
        return view;
    }

    public void bindView(View view, Context context, float amount, int position) {
        ListItemDetailHolder holder = (ListItemDetailHolder)view.getTag();
        mPerson.setCurrentItemPosition(position);
        holder.mItem.setText(context.getString(R.string.format_detail_item_label, position));
        holder.mAmt.setText(context.getString(R.string.format_amount, amount));
        holder.mAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.v("ItemAdapter", "afterTextChanged called position " + mPerson.getCurrentItemPosition());
                mPerson.setItem(mPerson.getCurrentItemPosition(), Float.parseFloat(s.toString()));
                mPerson.updateTotal();
                notifyDataSetChanged();
            }
        });

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemAdapter.add(0f);
            }
        });
    }

    public void add(float item) {
        mPerson.addItem(item);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mPerson.getItem(position);
    }
}
