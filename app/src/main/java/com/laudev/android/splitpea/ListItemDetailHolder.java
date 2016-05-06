package com.laudev.android.splitpea;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kevin on 5/6/16.
 */
public class ListItemDetailHolder {
    public final TextView mItem;
    public final EditText mAmt;
    public final Button mButton;

    public ListItemDetailHolder(View view) {
        mItem = (TextView)view.findViewById(R.id.detail_item_textview);
        mAmt = (EditText)view.findViewById(R.id.detail_item_edit_text);
        mButton = (Button)view.findViewById(R.id.detail_item_button);
    }
}
