package com.laudev.android.splitpea;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by kevin on 5/6/16.
 */
public class ListItemDetailHolder {
//    public int mPosition;
    public final TextView mItem;
    public final TextView mAmt;
    public final Button mButton;

    public ListItemDetailHolder(View view) {
//        mPosition = view.getId();
        mItem = (TextView)view.findViewById(R.id.detail_item_textview);
        mAmt = (TextView)view.findViewById(R.id.detail_item_value_textview);
        mButton = (Button)view.findViewById(R.id.detail_item_button);
    }
}
