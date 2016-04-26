package com.laudev.android.splitpea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private final String PARAM_NEW_EVENT = "newEvent";
    private final String PARAM_SUBTOTAL = "mSubtotal";
    private final String PARAM_TOTAL = "mTotal";

    private boolean newEvent = true;
    private float mSubtotal;
    private float tax;
    private float tip;
    private float mTotal;

    private EditText mSubtotalEditText;
    private EditText mTaxEditText;
    private EditText mTipEditText;
    private TextView mTotalTextView;
    private Button mAllocate;
    private Button mCalcTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find input fields
        mSubtotalEditText = (EditText)findViewById(R.id.subtotal);
        mTaxEditText = (EditText)findViewById(R.id.tax);
        mTipEditText = (EditText)findViewById(R.id.tip);
        mTotalTextView = (TextView)findViewById(R.id.total);

        // add button to allocate costs for now
        // remove this later
        mAllocate = (Button)findViewById(R.id.allocate);
        mAllocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllocateActivity.class)
                        .putExtra(PARAM_NEW_EVENT, newEvent)
                        .putExtra(PARAM_SUBTOTAL, mSubtotal)
                        .putExtra(PARAM_TOTAL, mTotal);
                startActivity(intent);
            }
        });

        // add button to calculate mTotal for now
        // remove this later
        mCalcTotal = (Button)findViewById(R.id.calc_total);
        mCalcTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTotal();
            }
        });
    }

    /*
        * get mSubtotal, tax and tip entries from screen
        * if values are not filled out, assumes default values
        */
    private void getParams() {
        if (mSubtotalEditText.getText() != null) {
            mSubtotal = Float.parseFloat(mSubtotalEditText.getText().toString());
        }
        if (mTaxEditText.getText() != null) {
            tax = Float.parseFloat(mTaxEditText.getText().toString());
        }
        if (mTipEditText.getText() != null) {
            tip = Float.parseFloat(mTipEditText.getText().toString());
        }
    }

    /*
    * update mTotal TextView based on input params
    */
    private void updateTotal() {
        //TODO update mTotal based on input parameters
        getParams();
        mTotal = mSubtotal * ( 1 + tax ) * ( 1 + tip );
        DecimalFormat df = new DecimalFormat("#.00");
        df.setRoundingMode(RoundingMode.CEILING);
        mTotalTextView.setText(df.format(mTotal));
    }
}
