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

    private boolean newPerson;
    private String name;
    private float subtotal;
    private float total;

    private final String PARAM_NEW_PERSON = "newPerson";
    private final String PARAM_NAME = "name";
    private final String PARAM_SUBTOTAL = "subtotal";
    private final String PARAM_TOTAL = "total";
    private final String PARAM_POSITION_ID = "positionId";

    private View mSubtotalFooterView;
    private View mTotalFooterView;

    private PersonAdapter mPersonsAdapter;

    public AllocateFragment() {
    }

    @Override
    public void onResume() {
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(PARAM_POSITION_ID) && mPersonsAdapter != null) {
            Person clickedPerson = (Person) mPersonsAdapter.getItem(intent.getIntExtra(PARAM_POSITION_ID, 0));
            clickedPerson.setName(intent.getStringExtra(PARAM_NAME));
            clickedPerson.setSubtotal(intent.getFloatExtra(PARAM_SUBTOTAL, 0f));
            mPersonsAdapter.notifyDataSetChanged();
        }
        super.onResume();
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
        Person[] summaryData = {new Person("Kevin", 10),
                                new Person("Melissa", 20),
                                new Person("Andrew", 30),
                                new Person("Haylee", 40)};
        List<Person> summaryList = new ArrayList<Person>(Arrays.asList(summaryData));

        // initialize adapter
        // TODO test this code
        mPersonsAdapter = new PersonAdapter(getActivity(), R.layout.listview_item_person, summaryList);

        // find and hook up adapter
        ListView personsListView = (ListView)rootView.findViewById(R.id.persons_listview);
        personsListView.setAdapter(mPersonsAdapter);

        // inflate footer Views
//        if (mSubtotalFooterView == null) {
//            mSubtotalFooterView = inflater.inflate(R.layout.listview_item_person, container, false);
//        }
//        if (mTotalFooterView == null) {
//            mTotalFooterView = inflater.inflate(R.layout.listview_item_person, container, false);
//        }
        // hook up footer data
//        ((TextView)mSubtotalFooterView).setText("" + subtotal);
//        ((TextView)mTotalFooterView).setText("" + total);
        // attach total data as footers
//        personsListView.addFooterView(mSubtotalFooterView);
//        personsListView.addFooterView(mTotalFooterView);

        // set onItemClick to launch detail activity
        personsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person = (Person)mPersonsAdapter.getItem(position);
                if (person != null) {
                    startActivity(new Intent(getActivity(), DetailActivity.class)
                            .putExtra(PARAM_POSITION_ID, position)
                            .putExtra(PARAM_NAME, person.getName())
                            .putExtra(PARAM_SUBTOTAL, person.getSubtotal()));
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
