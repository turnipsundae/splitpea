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
public class AllocateActivityFragment extends Fragment {

    private float subtotal;
    private float total;

    private ArrayAdapter<String> mPersonsAdapter;

    public AllocateActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_allocate, container, false);

        // get intent
        Intent intent = getActivity().getIntent();

        // test for null intents
        if (intent == null) {
            return null;
        } else {
            subtotal = intent.getFloatExtra("subtotal", 25);
            total = intent.getFloatExtra("total", 25);
        }

        // test data for listview
        String[] summaryData = {"Remainder: " + total, "Subtotal: " + subtotal, "Total: " + total};
        List<String> summaryList = new ArrayList<String>(Arrays.asList(summaryData));
        mPersonsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_item_person, R.id.listivew_item_text, summaryList);
        ListView personsListView = (ListView)rootView.findViewById(R.id.persons_listview);
        personsListView.setAdapter(mPersonsAdapter);
        personsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String details = (String)mPersonsAdapter.getItem(position);
                if (details != null) {
                    startActivity(new Intent(getActivity(), DetailActivity.class)
                            .putExtra("details", details));
                }
            }
        });

        return rootView;
    }
}
