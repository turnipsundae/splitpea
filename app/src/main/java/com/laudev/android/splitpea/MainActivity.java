package com.laudev.android.splitpea;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String PARAM_NEW_EVENT = "newEvent";
    private final String PARAM_EVENT_TOTAL = "mEventTotal";

    private final String DECIMAL = ".";
    private final String CORRECT_DECIMAL_FORMAT = "0.";

    private boolean newEvent = true;
    private Total mEventTotal;

    private EditText mSubtotalEditText;
    private EditText mTaxEditText;
    private EditText mTipEditText;
    private TextView mTotalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mEventTotal = new Total();
        }

        // find input fields
        mSubtotalEditText = (EditText)findViewById(R.id.subtotal);
        mSubtotalEditText.addTextChangedListener(mSubtotalTextWatcher);
        mTaxEditText = (EditText)findViewById(R.id.tax);
        mTaxEditText.addTextChangedListener(mTaxTextWatcher);
        mTipEditText = (EditText)findViewById(R.id.tip);
        mTipEditText.addTextChangedListener(mTipTextWatcher);
        mTotalTextView = (TextView)findViewById(R.id.total);

    }

    /*
    * update mTotal TextView based on input params
    */
    private void updateTotal() {
        mEventTotal.updateTotal();
        mTotalTextView.setText(String.format(getResources().getString(R.string.format_dollar_amount), mEventTotal.getTotal()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_confirm:
                Intent intent = new Intent(getApplicationContext(), AllocateActivity.class)
                        .putExtra(PARAM_NEW_EVENT, newEvent)
                        .putExtra(PARAM_EVENT_TOTAL, mEventTotal);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.v("MainActivity", "onSaveInstanceState called");
        outState.putParcelable(PARAM_EVENT_TOTAL, mEventTotal);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public TextWatcher mSubtotalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // check for blank string
            if (s.toString().length() > 0) {

                // Correct "." input to "0." before parsing
                if (s.toString().equals(DECIMAL)) {
                    mSubtotalEditText.setText(CORRECT_DECIMAL_FORMAT);
                    mSubtotalEditText.setSelection(mSubtotalEditText.getText().length());
                } else {
                    mEventTotal.setSubtotal(Float.parseFloat(s.toString()));
                    updateTotal();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher mTaxTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // check for blank string
            if (s.toString().length() > 0) {

                // Correct "." input to "0." before parsing
                if (s.toString().equals(DECIMAL)) {
                    mTaxEditText.setText(CORRECT_DECIMAL_FORMAT);
                    mTaxEditText.setSelection(mTaxEditText.getText().length());
                } else {
                    mEventTotal.setTaxPercent(Float.parseFloat(s.toString()));
                    updateTotal();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher mTipTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // check for blank string
            if (s.toString().length() > 0) {

                // Correct "." input to "0." before parsing
                if (s.toString().equals(DECIMAL)) {
                    mTipEditText.setText(CORRECT_DECIMAL_FORMAT);
                    mTipEditText.setSelection(mTipEditText.getText().length());
                } else {
                    mEventTotal.setTipPercent(Float.parseFloat(s.toString()));
                    updateTotal();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
