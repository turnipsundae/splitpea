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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RadioGroup mTipRadioGroup;
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
        mTipRadioGroup = (RadioGroup)findViewById(R.id.radio_group);
        mTipRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mid_radio_button:
                        mEventTotal.setTipPercent(getResources().getInteger(R.integer.tip_mid_default_value));
                        updateTotal();
                        break;
                    case R.id.high_radio_button:
                        mEventTotal.setTipPercent(getResources().getInteger(R.integer.tip_high_default_value));
                        updateTotal();
                        break;
                    default:
                        mEventTotal.setTipPercent(getResources().getInteger(R.integer.tip_low_default_value));
                        updateTotal();
                        break;
                }
            }
        });
//        if (mTipRadioGroup.getCheckedRadioButtonId() < 0) {
//            mTipRadioGroup.check(R.id.low_radio_button);
//        }
        mTotalTextView = (TextView)findViewById(R.id.total);

    }

    /*
    * update mAmt TextView based on input params
    */
    private void updateTotal() {
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

        }

        @Override
        public void afterTextChanged(Editable s) {
            // If blank string set total to 0
            if (s.toString().equals("") && mEventTotal.getSubtotal() != 0f) {
                mEventTotal.setSubtotal(0f);
                updateTotal();
            }
            // Check for blank string before auto calculate totals
            if (!s.toString().equals("")) {
                if (s.toString().charAt(0) == '.') {
                    s.insert(0, "0");
                } else {
                    mEventTotal.setSubtotal(Float.parseFloat(s.toString()));
                    updateTotal();
                }
            }
        }
    };

    public TextWatcher mTaxTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // If blank string set total to 0
            if (s.toString().equals("") && mEventTotal.getTaxAmt() != 0f) {
                mEventTotal.setTaxAmt(0f);
                updateTotal();
            }
            // Check for blank string before auto calculate totals
            if (!s.toString().equals("")) {
                if (s.toString().charAt(0) == '.') {
                    s.insert(0, "0");
                } else {
                    mEventTotal.setTaxAmt(Float.parseFloat(s.toString()));
                    updateTotal();
                }
            }
        }
    };
}
