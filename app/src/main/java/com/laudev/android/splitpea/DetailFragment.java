package com.laudev.android.splitpea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private static final String PARAM_PERSON = "person";

    private boolean newPerson;
    private int positionId;
    private Person person;

    // references to XML views
    private EditText mNameEditText;
    private EditText mDetailedItemEditText;


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
        person.setName(
                mNameEditText.getText().toString());
        person.setSubtotal(
                Float.parseFloat(mDetailedItemEditText.getText().toString()));
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

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            getParamsFromIntent(intent);
        }

        if (newPerson) {
            // if new person, create a new person
            person = new Person();
        } else {
            // initialize text views with existing person data
            mNameEditText.setText(person.getName());
            mDetailedItemEditText.setText(Float.toString(person.getSubtotal()));
        }

        return rootView;
    }

    private void getParamsFromIntent(Intent intent) {
        // is this new person or modify existing?
        if (intent.hasExtra(PARAM_NEW_PERSON)) {
            newPerson = intent.getBooleanExtra(PARAM_NEW_PERSON, true);
        }

        if (!newPerson) {
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
    }
}
