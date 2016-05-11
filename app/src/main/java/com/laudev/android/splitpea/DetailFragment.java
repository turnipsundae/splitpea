package com.laudev.android.splitpea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private final String PARAM_NEW_PERSON = "newPerson";
    private final String PARAM_POSITION_ID = "positionId";
    private final String PARAM_PERSON = "person";
    private final String PARAM_SUBTOTAL_REMAINING = "mSubtotalRemaining";
    private final String DECIMAL = ".";
    private final String CORRECT_DECIMAL_FORMAT = "0.";

    private boolean newPerson;
    private int positionId;
    private Person person;
    private float mSubtotalRemaining;
    private ItemAdapter mItemAdapter;
    private List<Item> mItemList;

    // references to XML views
    private TextView mSubtotalRemainingTextView;
    private EditText mNameEditText;
    private EditText mDetailedItemEditText;
    private TextView mSubtotalTextView;
    private TextView mTaxTextView;
    private EditText mTipEditText;
    private TextView mTotalTextView;
    private EditText mItemEditText1;
    private EditText mItemEditText2;
    private EditText mItemEditText3;

    private EditText mFooterAddItemValue;
    private TextView mFooterSubtotalValue;
    private TextView mFooterTaxValue;
    private TextView mFooterTipValue;
    private TextView mFooterTipPercent;
    private RadioGroup mFooterTipRadioGroup;
    private TextView mFooterTotalValue;


    public DetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detailfragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_confirm:
                updateParams();
                getActivity().setResult(Activity.RESULT_OK, confirmPersonDetailIntent());
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent confirmPersonDetailIntent() {
        return new Intent(getActivity(), AllocateActivity.class)
                .putExtra(PARAM_NEW_PERSON, newPerson)
                .putExtra(PARAM_POSITION_ID, positionId)
                .putExtra(PARAM_PERSON, person);
    }

    private void updateParams() {
        if (!mNameEditText.getText().toString().equals("")) {
            person.setName(mNameEditText.getText().toString());
        }
        if (!mFooterAddItemValue.getText().toString().equals("")) {
            person.addItem(new Item("Item", Float.parseFloat(mFooterAddItemValue.getText().toString())));
            person.updateTotal();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("DetailFragment", "onCreate called");

        // if new instance
        if (savedInstanceState == null) {
            // initialize summaryList with no data
            mItemList = new ArrayList<>();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Get intent
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            getParamsFromIntent(intent);
        }

        // Find views that don't need to be initialized with Person details yet
        mSubtotalRemainingTextView = (TextView)rootView.findViewById(R.id.subtotal_remaining_text_view);
        mSubtotalRemainingTextView.setText(getString(R.string.format_dollar_amount, mSubtotalRemaining));
        mNameEditText = (EditText)rootView.findViewById(R.id.name_edit_text);

        // initialize detail items adapter
        mItemAdapter = new ItemAdapter(getActivity(), R.layout.listview_item_detail, mItemList);

        // find and hook up adapter
        ListView itemsListView = (ListView) rootView.findViewById(R.id.items_listview);
        itemsListView.setAdapter(mItemAdapter);

        // get person shell with tax and tip pre-entered
        addFooterViews(getContext(), itemsListView);

        if (!newPerson) {
            // initialize text views with existing person data
            mNameEditText.setText(person.getName());
            updateTotal();
        }

        return rootView;
    }

    private void getParamsFromIntent(Intent intent) {
        // is this new person or modify existing?
        if (intent.hasExtra(PARAM_NEW_PERSON)) {
            newPerson = intent.getBooleanExtra(PARAM_NEW_PERSON, true);
        }
        // get position of item called so edits can be returned
        if (intent.hasExtra(PARAM_POSITION_ID)) {
            positionId = intent.getIntExtra(PARAM_POSITION_ID, 0);
        }
        // get person object
        if (intent.hasExtra(PARAM_PERSON)) {
            person = intent.getParcelableExtra(PARAM_PERSON);
            mItemList = person.getItems();
        }
        // get event totals
        if (intent.hasExtra(PARAM_SUBTOTAL_REMAINING)) {
            mSubtotalRemaining = intent.getFloatExtra(PARAM_SUBTOTAL_REMAINING, 0f);
        }
//        Log.v("DetailFragment", "Extracted Parceled person. New? " + newPerson +
//                " #" + positionId + " " + person.getName() + " " + person.getSubtotal() +
//                " items: " + person.getItems() +
//                " Event subtotal: " + mSubtotalRemaining);
    }

    public TextWatcher mItemTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.v("TextWatcher", "Editable contains " + s.toString());
            // Check for blank string before auto calculate totals
            if (!s.toString().equals("")) {
                if (s.toString().charAt(0) == '.') {
                    s.insert(0, "0");
                } else {
                    float tempAmt = Float.parseFloat(s.toString());
                    updateSubtotalRemaining(tempAmt);
                    updateSubtotal(tempAmt);
                    updateTax(tempAmt);
                    updateTip(tempAmt);
                    updateTotal(tempAmt);
                }
            }
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
                    person.setTipPercent(Float.parseFloat(s.toString()));
                    updateTotal();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    // update listview footer with eventTotal
    private void addFooterViews(Context context, ListView listView) {
        // get inflater and inflate footer view
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate item adder
        View footerAddItemView = inflater.inflate(R.layout.listview_add_item, null, false);
        TextView addItemName = (TextView)footerAddItemView.findViewById(R.id.detail_item_textview);
        addItemName.setText(getString(R.string.new_item));
        mFooterAddItemValue = (EditText)footerAddItemView.findViewById(R.id.detail_item_edit_text);
        mFooterAddItemValue.addTextChangedListener(mItemTextWatcher);
        Button addItemButton = (Button)footerAddItemView.findViewById(R.id.detail_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DetailFragment", "AddPersonButton pressed");
                mItemAdapter.add(new Item("Item", Float.parseFloat(mFooterAddItemValue.getText().toString())));
                updateSubtotalRemaining();
                updateSubtotal();
                updateTax();
                updateTip();
                updateTotal();
                mFooterAddItemValue.setText("");
                mFooterAddItemValue.requestFocus();
                mFooterAddItemValue.setSelection(0);
            }
        });
        listView.addFooterView(footerAddItemView);

        // inflate subtotal
        View footerSubtotalView = inflater.inflate(R.layout.listview_item_footer, null, false);
        TextView subtotalName = (TextView) footerSubtotalView.findViewById(R.id.name_textview);
        subtotalName.setText(getString(R.string.subtotal));
        mFooterSubtotalValue = (TextView) footerSubtotalView.findViewById(R.id.amt_textview);
        mFooterSubtotalValue.setText(context.getString(R.string.format_dollar_amount, person.getSubtotal()));
        listView.addFooterView(footerSubtotalView);

        // inflate tax
        View footerTaxView = inflater.inflate(R.layout.listview_item_footer, null, false);
        TextView taxName = (TextView) footerTaxView.findViewById(R.id.name_textview);
        taxName.setText(getString(R.string.format_text_pct_label, getString(R.string.tax), person.getTaxPercent()));
        mFooterTaxValue = (TextView) footerTaxView.findViewById(R.id.amt_textview);
        mFooterTaxValue.setText(context.getString(R.string.format_dollar_amount, person.getTaxAmt()));
        listView.addFooterView(footerTaxView);

        // inflate tip
        View footerTipView = inflater.inflate(R.layout.listview_item_footer, null, false);
        mFooterTipPercent = (TextView) footerTipView.findViewById(R.id.name_textview);
        mFooterTipPercent.setText(getString(R.string.format_text_pct_label, getString(R.string.tip), person.getTipPercent()));
        mFooterTipValue = (TextView) footerTipView.findViewById(R.id.amt_textview);
        mFooterTipValue.setText(context.getString(R.string.format_dollar_amount, person.getTipAmt()));
        listView.addFooterView(footerTipView);

        // inflate tip editor
        View footerTipRadioGroupView = inflater.inflate(R.layout.listview_item_footer_choice, null, false);
        mFooterTipRadioGroup = (RadioGroup) footerTipRadioGroupView.findViewById(R.id.radio_group);
        mFooterTipRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.low_radio_button:
                        person.setTipPercent(getResources().getInteger(R.integer.tip_low_default_value));
                        mFooterTipPercent.setText(getString(R.string.format_text_pct_label, getString(R.string.tip), person.getTipPercent()));
                        if (mFooterAddItemValue.getText().toString().length() > 0) {
                            float tempAmt = Float.parseFloat(mFooterAddItemValue.getText().toString());
                            updateTip(tempAmt);
                            updateTotal(tempAmt);
                        } else {
                            updateTip();
                            updateTotal();
                        }
                        break;
                    case R.id.mid_radio_button:
                        person.setTipPercent(getResources().getInteger(R.integer.tip_mid_default_value));
                        mFooterTipPercent.setText(getString(R.string.format_text_pct_label, getString(R.string.tip), person.getTipPercent()));
                        if (mFooterAddItemValue.getText().toString().length() > 0) {
                            float tempAmt = Float.parseFloat(mFooterAddItemValue.getText().toString());
                            updateTip(tempAmt);
                            updateTotal(tempAmt);
                        } else {
                            updateTip();
                            updateTotal();
                        }
                        break;
                    case R.id.high_radio_button:
                        person.setTipPercent(getResources().getInteger(R.integer.tip_high_default_value));
                        mFooterTipPercent.setText(getString(R.string.format_text_pct_label, getString(R.string.tip), person.getTipPercent()));
                        if (mFooterAddItemValue.getText().toString().length() > 0) {
                            float tempAmt = Float.parseFloat(mFooterAddItemValue.getText().toString());
                            updateTip(tempAmt);
                            updateTotal(tempAmt);
                        } else {
                            updateTip();
                            updateTotal();
                        }
                        break;
                }
            }
        });
        listView.addFooterView(footerTipRadioGroupView);

        // inflate total
        View footerTotalView = inflater.inflate(R.layout.listview_item_footer, null, false);
        TextView totalName = (TextView) footerTotalView.findViewById(R.id.name_textview);
        totalName.setText(getString(R.string.total));
        mFooterTotalValue = (TextView) footerTotalView.findViewById(R.id.amt_textview);
        mFooterTotalValue.setText(this.getString(R.string.format_dollar_amount, person.getTotal()));
        listView.addFooterView(footerTotalView);

        // check default tip radio button
        if (Math.abs(person.getTipPercent() - getResources().getInteger(R.integer.tip_low_default_value)) < 0.001f) {
            mFooterTipRadioGroup.check(R.id.low_radio_button);
        }
        if (Math.abs(person.getTipPercent() - getResources().getInteger(R.integer.tip_mid_default_value)) < 0.001f) {
            mFooterTipRadioGroup.check(R.id.mid_radio_button);
        }
        if (Math.abs(person.getTipPercent() - getResources().getInteger(R.integer.tip_high_default_value)) < 0.001f) {
            mFooterTipRadioGroup.check(R.id.high_radio_button);
        }
    }

    private void updateSubtotalRemaining() {
        mSubtotalRemainingTextView.setText(getString(R.string.format_dollar_amount, mSubtotalRemaining - person.getSubtotal()));
    }

    private void updateSubtotalRemaining(float tempAmt) {
        mSubtotalRemainingTextView.setText(getString(R.string.format_dollar_amount, mSubtotalRemaining - tempAmt));
    }

    private void updateSubtotal() {
        mFooterSubtotalValue.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getSubtotal()));
    }

    private void updateSubtotal(float tempAmt) {
        mFooterSubtotalValue.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getSubtotal() + tempAmt));
    }

    private void updateTax() {
        mFooterTaxValue.setText(getString(R.string.format_amount, person.getTaxAmt()));
    }

    private void updateTax(float tempAmt) {
        mFooterTaxValue.setText(getString(R.string.format_amount, person.getTaxAmt() + tempAmt * person.getTaxPercent() / 100f));
    }

    private void updateTip() {
        mFooterTipValue.setText(getString(R.string.format_amount, person.getTipAmt()));
    }

    private void updateTip(float tempAmt) {
        mFooterTipValue.setText(getString(R.string.format_amount, person.getTipAmt() + tempAmt * person.getTipPercent() / 100f));
    }

    private void updateTotal() {
        mFooterTotalValue.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getTotal()));
    }

    private void updateTotal(float tempAmt) {
        mFooterTotalValue.setText(String.format(getResources().getString(R.string.format_dollar_amount),
                person.getTotal() +
                        tempAmt +
                        (tempAmt * person.getTaxPercent() / 100f) +
                        (tempAmt * person.getTipPercent() / 100f)));
    }
}
