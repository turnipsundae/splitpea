package com.laudev.android.splitpea;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_NAME = "name";
    private static final String PARAM_SUBTOTAL = "subtotal";
    private static final String PARAM_TOTAL = "total";

    // TODO: Rename and change types of parameters
    private String name;
    private float subtotal;
    private float total;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param subtotal sum of all detailed items.
     * @param total sum of subtotal and tax.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(float subtotal, float total) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putFloat(PARAM_SUBTOTAL, subtotal);
        args.putFloat(PARAM_TOTAL, total);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subtotal = getArguments().getFloat(PARAM_SUBTOTAL);
            total = getArguments().getFloat(PARAM_TOTAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView detailTextView = (TextView)rootView.findViewById(R.id.detail_textview);
        EditText nameEditText = (EditText)rootView.findViewById(R.id.name_edit_text);
        EditText detailedItemEditText = (EditText)rootView.findViewById(R.id.detail_item_edit_text);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            name = intent.getStringExtra(PARAM_NAME);
            subtotal = intent.getFloatExtra(PARAM_SUBTOTAL, 0);
            total = intent.getFloatExtra(PARAM_TOTAL, 0);
        }

        nameEditText.setText(name);

        if (subtotal == 0f) {
            detailedItemEditText.setHint(Float.toString(subtotal));
        } else {
            detailedItemEditText.setText(Float.toString(subtotal));
        }

        return rootView;
    }
}
