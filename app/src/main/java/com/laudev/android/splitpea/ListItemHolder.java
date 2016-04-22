package com.laudev.android.splitpea;

import android.view.View;
import android.widget.TextView;

/**
 * Created by kevin on 4/21/16.
 */
public class ListItemHolder {
    public final TextView mName;
    public final TextView mSubtotal;

    public ListItemHolder(View view) {
        mName = (TextView)view.findViewById(R.id.name_textview);
        mSubtotal = (TextView)view.findViewById(R.id.subtotal_textview);
    }
}
