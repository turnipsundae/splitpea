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
//        mItemAdapter = new ItemAdapter(getActivity(), R.layout.listview_item_detail, person);
        mItemAdapter = new ItemAdapter(getActivity(), R.layout.listview_item_detail, mItemList);

        // find and hook up adapter
        ListView itemsListView = (ListView) rootView.findViewById(R.id.items_listview);
        itemsListView.setAdapter(mItemAdapter);

        // get person shell with tax and tip pre-entered
        addFooterViews(getContext(), itemsListView, person);

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
            // check for blank string
            if (s.toString().length() > 0) {

                // Correct "." input to "0." before parsing
//                if (s.toString().equals(DECIMAL)) {
//                    mDetailedItemEditText.setText(CORRECT_DECIMAL_FORMAT);
//                    mDetailedItemEditText.setSelection(mDetailedItemEditText.getText().length());
//                } else {
//                    person.addItem(Float.parseFloat(s.toString()));
//                    updateSubtotalRemaining();
//                    updateSubtotal();
//                    updateTax();
//                    updateTotal();
//                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
//            int count = 0;
//            while (count < s.length()) {
//                if (s.toString().charAt(count) == '.') {
//                    if (count - 1 < 0) {
//                        s.insert(count, );
//                    }
//                }
//                count++;
//            }
            Log.v("TextWatcher", "Editable contains " + s.toString());
            if (!s.toString().equals("")) {
                if (s.toString().charAt(0) == '.') {
                    s.insert(0, "0");
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
    private void addFooterViews(Context context, ListView listView, Person person) {
        // get inflater and inflate footer view
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate item adder
        View footerAddItemView = inflater.inflate(R.layout.listview_add_item, null, false);
        TextView addItemName = (TextView)footerAddItemView.findViewById(R.id.detail_item_textview);
        addItemName.setText(getString(R.string.item));
        mFooterAddItemValue = (EditText)footerAddItemView.findViewById(R.id.detail_item_edit_text);
        mFooterAddItemValue.addTextChangedListener(mItemTextWatcher);
        Button addItemButton = (Button)footerAddItemView.findViewById(R.id.detail_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DetailFragment", "AddPersonButton pressed");
                mItemAdapter.add(new Item("Item", Float.parseFloat(mFooterAddItemValue.getText().toString())));
                updateSubtotal();
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
        taxName.setText(getString(R.string.tax));
        TextView taxValue = (TextView) footerTaxView.findViewById(R.id.amt_textview);
        taxValue.setText(context.getString(R.string.format_tax_tip, person.getTaxPercent()));
        listView.addFooterView(footerTaxView);

        // inflate tip
        View footerTipView = inflater.inflate(R.layout.listview_item_footer_edit, null, false);
        TextView tipName = (TextView) footerTipView.findViewById(R.id.detail_item_textview);
        tipName.setText(getString(R.string.tip));
        TextView tipValue = (TextView) footerTipView.findViewById(R.id.detail_item_edit_text);
        tipValue.setText(context.getString(R.string.format_amount, person.getTipPercent()));
        listView.addFooterView(footerTipView);

        // inflate total
        View footerTotalView = inflater.inflate(R.layout.listview_item_footer, null, false);
        TextView totalName = (TextView) footerTotalView.findViewById(R.id.name_textview);
        totalName.setText(getString(R.string.total));
        mFooterTotalValue = (TextView) footerTotalView.findViewById(R.id.amt_textview);
        mFooterTotalValue.setText(this.getString(R.string.format_dollar_amount, person.getTotal()));
        listView.addFooterView(footerTotalView);
    }

    private float getSubtotalRemaining() {
        return mSubtotalRemaining - person.getSubtotal();
    }

    private void updateSubtotalRemaining() {
        mSubtotalRemainingTextView.setText(getString(R.string.format_dollar_amount, getSubtotalRemaining()));
    }

    private void updateSubtotal() {
        mFooterSubtotalValue.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getSubtotal()));
    }

    private void updateTax() {
        mTaxTextView.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getTaxAmt()));
    }

    private void updateTotal() {
//        mTotalTextView.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getTotal()));
        mFooterTotalValue.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getTotal()));
    }
}
