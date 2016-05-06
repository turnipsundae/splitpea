package com.laudev.android.splitpea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        if (mNameEditText.getText() != null) {
            person.setName(mNameEditText.getText().toString());
        }
//        if (mDetailedItemEditText.getText().toString().length() > 0) {
//            person.setSubtotal(Float.parseFloat(mDetailedItemEditText.getText().toString()));
//        }
//        if (mTaxTextView.getText().toString().length() > 0) {
//            person.setTaxPercent(Float.parseFloat(mTaxTextView.getText().toString()));
//        }
//        if (mTipEditText.getText().toString().length() > 0) {
//            person.setTipPercent(Float.parseFloat(mTipEditText.getText().toString()));
//        }
//        person.updateTotal();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            subtotal = getArguments().getFloat(PARAM_SUBTOTAL);
//            total = getArguments().getFloat(PARAM_TOTAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            getParamsFromIntent(intent);
        }

        mSubtotalRemainingTextView = (TextView)rootView.findViewById(R.id.subtotal_remaining_text_view);
        mNameEditText = (EditText)rootView.findViewById(R.id.name_edit_text);
        mDetailedItemEditText = (EditText)rootView.findViewById(R.id.detail_item_edit_text);
        mSubtotalTextView = (TextView)rootView.findViewById(R.id.subtotal_text_view);
        mTaxTextView = (TextView)rootView.findViewById(R.id.tax_text_view);
        mTipEditText = (EditText)rootView.findViewById(R.id.tip_edit_text);
        mTotalTextView = (TextView)rootView.findViewById(R.id.total_text_view);
//        mItemEditText1 = (EditText)rootView.findViewById(R.id.detail_item_edit_text);
        mItemEditText2 = (EditText)rootView.findViewById(R.id.detail_item_edit_text_2);
        mItemEditText3 = (EditText)rootView.findViewById(R.id.detail_item_edit_text_3);

        // initialize adapter
        if (!newPerson) {
            mItemAdapter = new ItemAdapter(getActivity(), R.layout.listview_item_detail, person.getItems());
        } else {
            mItemAdapter = new ItemAdapter(getActivity(), R.layout.listview_item_detail, new float[]{0});
        }

        // find and hook up adapter
        ListView itemsListView = (ListView) rootView.findViewById(R.id.items_listview);
        itemsListView.setAdapter(mItemAdapter);

        // get person shell with tax and tip pre-entered
        mSubtotalRemainingTextView.setText(getString(R.string.format_dollar_amount, mSubtotalRemaining));
        mDetailedItemEditText.addTextChangedListener(mItemTextWatcher);
        mItemEditText2.addTextChangedListener(mItemTextWatcher2);
        mItemEditText3.addTextChangedListener(mItemTextWatcher3);
        mTaxTextView.setText(Float.toString(person.getTaxAmt()));
        mTipEditText.setText(Float.toString(person.getTipPercent()));
        mTipEditText.addTextChangedListener(mTipTextWatcher);

        if (!newPerson) {
            // initialize text views with existing person data
            mNameEditText.setText(person.getName());
            mDetailedItemEditText.setText(Float.toString(person.getSubtotal()));
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
        }
        // get event totals
        if (intent.hasExtra(PARAM_SUBTOTAL_REMAINING)) {
            mSubtotalRemaining = intent.getFloatExtra(PARAM_SUBTOTAL_REMAINING, 0f);
        }
        Log.v("DetailFragment", "Extracted Parceled person. New? " + newPerson +
                " #" + positionId + " " + person.getName() + " " + person.getSubtotal() +
                " Event subtotal: " + mSubtotalRemaining);
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
                if (s.toString().equals(DECIMAL)) {
                    mDetailedItemEditText.setText(CORRECT_DECIMAL_FORMAT);
                    mDetailedItemEditText.setSelection(mDetailedItemEditText.getText().length());
                } else {
                    person.addItem(Float.parseFloat(s.toString()));
                    updateSubtotalRemaining();
                    updateSubtotal();
                    updateTax();
                    updateTotal();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher mItemTextWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // check for blank string
            if (s.toString().length() > 0) {

                // Correct "." input to "0." before parsing
                if (s.toString().equals(DECIMAL)) {
                    mItemEditText2.setText(CORRECT_DECIMAL_FORMAT);
                    mItemEditText2.setSelection(mItemEditText2.getText().length());
                } else {
                    person.addItem(Float.parseFloat(s.toString()));
                    updateSubtotalRemaining();
                    updateSubtotal();
                    updateTax();
                    updateTotal();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher mItemTextWatcher3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // check for blank string
            if (s.toString().length() > 0) {

                // Correct "." input to "0." before parsing
                if (s.toString().equals(DECIMAL)) {
                    mItemEditText3.setText(CORRECT_DECIMAL_FORMAT);
                    mItemEditText3.setSelection(mItemEditText3.getText().length());
                } else {
                    person.addItem(Float.parseFloat(s.toString()));
                    updateSubtotalRemaining();
                    updateSubtotal();
                    updateTax();
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
                    person.setTipPercent(Float.parseFloat(s.toString()));
                    updateTotal();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private float getSubtotalRemaining() {
        return mSubtotalRemaining - person.getSubtotal();
    }

    private void updateSubtotalRemaining() {
        mSubtotalRemainingTextView.setText(getString(R.string.format_dollar_amount, getSubtotalRemaining()));
    }

    private void updateSubtotal() {
        mSubtotalTextView.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getSubtotal()));
    }

    private void updateTax() {
        mTaxTextView.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getTaxAmt()));
    }

    private void updateTotal() {
        mTotalTextView.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getTotal()));
    }
}
