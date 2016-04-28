package com.laudev.android.splitpea;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private final String PARAM_NEW_EVENT = "newEvent";
    private final String PARAM_EVENT_TOTAL = "mEventTotal";

    private boolean newEvent = true;
    private Total mEventTotal;

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

        if (savedInstanceState == null) {
            mEventTotal = new Total();
        }

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
                        .putExtra(PARAM_EVENT_TOTAL, mEventTotal);
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
            Float mSubtotal = Float.parseFloat(mSubtotalEditText.getText().toString());
            mEventTotal.setSubtotal(mSubtotal);
        }
        if (mTaxEditText.getText() != null) {
            Float tax = Float.parseFloat(mTaxEditText.getText().toString());
            mEventTotal.setTax(tax);
        }
        if (mTipEditText.getText() != null) {
            Float tip = Float.parseFloat(mTipEditText.getText().toString());
            mEventTotal.setTip(tip);
        }
    }

    /*
    * update mTotal TextView based on input params
    */
    private void updateTotal() {
        //TODO update mTotal based on input parameters
        getParams();
        mEventTotal.updateTotal();
        String total = this.getString(R.string.format_dollar_amount, mEventTotal.getTotal());
        mTotalTextView.setText(total);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.v("MainActivity", "onSaveInstanceState called");
        outState.putParcelable(PARAM_EVENT_TOTAL, mEventTotal);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
