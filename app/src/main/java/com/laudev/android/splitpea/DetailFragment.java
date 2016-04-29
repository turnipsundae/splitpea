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
import android.widget.TextView;

import org.w3c.dom.Text;


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
    private final String DECIMAL = ".";
    private final String CORRECT_DECIMAL_FORMAT = "0.";

    private boolean newPerson;
    private int positionId;
    private Person person;

    // references to XML views
    private EditText mNameEditText;
    private EditText mDetailedItemEditText;
    private TextView mSubtotalTextView;
    private EditText mTaxEditText;
    private EditText mTipEditText;
    private TextView mTotalTextView;


    public DetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param subtotal sum of all detailed items.
     * @param total sum of subtotal and tax.
     * @return A new instance of fragment DetailFragment.
     */
//    public static DetailFragment newInstance(float subtotal, float total) {
//        DetailFragment fragment = new DetailFragment();
//        Bundle args = new Bundle();
//        args.putFloat(PARAM_SUBTOTAL, subtotal);
//        args.putFloat(PARAM_TOTAL, total);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
            case R.id.action_confirm_person:
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
        if (mDetailedItemEditText.getText().toString().length() > 0) {
            person.setSubtotal(Float.parseFloat(mDetailedItemEditText.getText().toString()));
        }
        if (mTaxEditText.getText().toString().length() > 0) {
            person.setTax(Float.parseFloat(mTaxEditText.getText().toString()));
        }
        if (mTipEditText.getText().toString().length() > 0) {
            person.setTip(Float.parseFloat(mTipEditText.getText().toString()));
        }
        person.updateTotal();
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
        mNameEditText = (EditText)rootView.findViewById(R.id.name_edit_text);
        mDetailedItemEditText = (EditText)rootView.findViewById(R.id.detail_item_edit_text);
        mSubtotalTextView = (TextView)rootView.findViewById(R.id.subtotal_text_view);
        mTaxEditText = (EditText)rootView.findViewById(R.id.tax_edit_text);
        mTipEditText = (EditText)rootView.findViewById(R.id.tip_edit_text);
        mTotalTextView = (TextView)rootView.findViewById(R.id.total_text_view);


        Intent intent = getActivity().getIntent();
        if (intent != null) {
            getParamsFromIntent(intent);
        }

        // get person shell with tax and tip pre-entered
        mDetailedItemEditText.addTextChangedListener(mItemTextWatcher);
        mTaxEditText.setText(Float.toString(person.getTax()));
        mTaxEditText.addTextChangedListener(mTaxTextWatcher);
        mTipEditText.setText(Float.toString(person.getTip()));
        mTipEditText.addTextChangedListener(mTipTextWatcher);

        if (!newPerson) {
            // initialize text views with existing person data
            mNameEditText.setText(person.getName());
            mDetailedItemEditText.setText(Float.toString(person.getSubtotal()));
//            mTaxEditText.setText(Float.toString(person.getTax()));
//            mTaxEditText.addTextChangedListener(mTaxTextWatcher);
//            mTipEditText.setText(Float.toString(person.getTip()));
//            mTipEditText.addTextChangedListener(mTipTextWatcher);
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
        Log.v("DetailFragment", "Extracted Parceled person. New? " + newPerson +
                " #" + positionId + " " + person.getName() + " " + person.getSubtotal());
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
                    person.setSubtotal(Float.parseFloat(s.toString()));
                    updateSubtotal();
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
                    person.setTax(Float.parseFloat(s.toString()));
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
                    person.setTip(Float.parseFloat(s.toString()));
                    updateTotal();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateSubtotal() {
        mSubtotalTextView.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getSubtotal()));
    }

    private void updateTotal() {
        person.updateTotal();
        mTotalTextView.setText(String.format(getResources().getString(R.string.format_dollar_amount), person.getTotal()));
    }
}
