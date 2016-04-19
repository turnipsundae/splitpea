package com.laudev.android.splitpea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private float subtotal;
    private float tax;
    private float tip;
    private float total;

    private EditText mSubtotalEditText;
    private EditText mTaxEditText;
    private EditText mTipEditText;
    private TextView mTotalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find input fields
        mSubtotalEditText = (EditText)findViewById(R.id.subtotal);
        mTaxEditText = (EditText)findViewById(R.id.tax);
        mTipEditText = (EditText)findViewById(R.id.tip);
        mTotalTextView= (TextView)findViewById(R.id.total);
    }

    /*
    * get subtotal, tax and tip entries from screen
    * if values are not filled out, assumes default values
    */
    private void getParams() {
        //TODO get input parameters
    }

    private void updateTotal() {
        //TODO update total based on input parameters
        getParams();
    }
}
