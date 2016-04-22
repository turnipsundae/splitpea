package com.laudev.android.splitpea;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllocateFragment extends Fragment {

    private float subtotal;
    private float total;

    private final String PARAM_SUBTOTAL = "subtotal";
    private final String PARAM_TOTAL = "total";

    private View mSubtotalFooterView;
    private View mTotalFooterView;

    private ArrayAdapter<String> mPersonsAdapter;

    public AllocateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_allocate, container, false);

        // get intent and params
        if (savedInstanceState != null) {
            subtotal = savedInstanceState.getFloat(PARAM_SUBTOTAL);
            total = savedInstanceState.getFloat(PARAM_TOTAL);
        } else {
            Intent intent = getActivity().getIntent();
            subtotal = intent.getFloatExtra(PARAM_SUBTOTAL, 25);
            total = intent.getFloatExtra(PARAM_TOTAL, 25);
        }

        // test data for listview
        String[] summaryData = {"Remainder: " + total, "Subtotal: " + subtotal, "Total: " + total};
        List<String> summaryList = new ArrayList<String>(Arrays.asList(summaryData));

        // inflate footer Views
        if (mSubtotalFooterView == null) {
            mSubtotalFooterView = inflater.inflate(R.layout.listview_item_person, container, false);
        }
        if (mTotalFooterView == null) {
            mTotalFooterView = inflater.inflate(R.layout.listview_item_person, container, false);
        }

        // hook up footer data
        ((TextView)mSubtotalFooterView).setText("" + subtotal);
        ((TextView)mTotalFooterView).setText("" + total);

        // initialize adapter
        mPersonsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_item_person, R.id.listivew_item_text, summaryList);

        // find and hook up adapter
        ListView personsListView = (ListView)rootView.findViewById(R.id.persons_listview);
        personsListView.setAdapter(mPersonsAdapter);

        // attach total data as footers
        personsListView.addFooterView(mSubtotalFooterView);
        personsListView.addFooterView(mTotalFooterView);

        // set onItemClick to launch detail activity
        personsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String details = (String)mPersonsAdapter.getItem(position);
                if (details != null) {
                    startActivity(new Intent(getActivity(), DetailActivity.class)
                            .putExtra(PARAM_SUBTOTAL, subtotal)
                            .putExtra(PARAM_TOTAL, total));
                }
            }
        });

        // add a FAB


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putFloat(PARAM_SUBTOTAL, subtotal);
        outState.putFloat(PARAM_TOTAL, total);
        super.onSaveInstanceState(outState);
    }


}
