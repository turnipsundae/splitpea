package com.laudev.android.splitpea;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
    private float mSubtotal;
    private float mTotal;
    private List<Person> summaryList;

    private final String PARAM_NEW_EVENT = "newEvent";
    private final String PARAM_NEW_PERSON = "newPerson";
    private final String PARAM_POSITION_ID = "positionId";
    private final String PARAM_SUBTOTAL = "mSubtotal";
    private final String PARAM_TOTAL = "mTotal";
    private final String PARAM_PERSON = "person";
    private final String PARAM_PERSONS_ARRAY = "persons";
    private final int PERSON_DETAIL_REQUEST = 1;
    private final int ADD_PERSON_REQUEST = 2;

    private View mSubtotalFooterView;
    private View mTotalFooterView;

    private PersonAdapter mPersonsAdapter;

    public AllocateFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PERSON_DETAIL_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    int positionId = data.getIntExtra(PARAM_POSITION_ID, 0);
                    Person person = data.getParcelableExtra(PARAM_PERSON);
                    Person clickedPerson = (Person)mPersonsAdapter.getItem(positionId);
                    clickedPerson.setName(person.getName());
                    clickedPerson.setSubtotal(person.getSubtotal());
                    mPersonsAdapter.notifyDataSetChanged();
                }
                break;
            case ADD_PERSON_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Person person = data.getParcelableExtra(PARAM_PERSON);
                    mPersonsAdapter.add(person);
                }
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("AllocateFragment", "onCreate called");

        // if new instance
        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();

            // check if brand new activity
            if (intent.hasExtra(PARAM_NEW_EVENT) && intent.getBooleanExtra(PARAM_NEW_EVENT, true)) {
                mSubtotal = intent.getFloatExtra(PARAM_SUBTOTAL, 0f);
                mTotal = intent.getFloatExtra(PARAM_TOTAL, 0f);
                Log.v("AllocateFragment", "Subtotal/Total: " + mSubtotal + "/" + mTotal);
            }

            // test data for listview
            Person[] persons = {new Person("Kevin", 10),
                    new Person("Melissa", 20),
                    new Person("Andrew", 30),
                    new Person("Haylee", 40)};
            summaryList = new ArrayList<>(Arrays.asList(persons));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("AllocateFragment", "onCreateView called");
        View rootView = inflater.inflate(R.layout.fragment_allocate, container, false);

        // initialize adapter
        mPersonsAdapter = new PersonAdapter(getActivity(), R.layout.listview_item_person, summaryList);

        // find and hook up adapter
        ListView personsListView = (ListView) rootView.findViewById(R.id.persons_listview);
        personsListView.setAdapter(mPersonsAdapter);

        // set click listener
        personsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person = (Person) mPersonsAdapter.getItem(position);
                startActivityForResult(new Intent(getActivity(), DetailActivity.class)
                        .putExtra(PARAM_NEW_PERSON, false)
                        .putExtra(PARAM_POSITION_ID, position)
                        .putExtra(PARAM_PERSON, person),
                        PERSON_DETAIL_REQUEST);
            }
        });

        // find FAB from parent
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(getActivity(), DetailActivity.class)
                                    .putExtra(PARAM_NEW_PERSON, true),
                            ADD_PERSON_REQUEST);
                }
            });
        }
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

        return rootView;
    }

    private void updatePersonsWithIntent(Intent intent) {
        // check if intent is null
        if (intent != null && intent.hasExtra(PARAM_NEW_PERSON) && mPersonsAdapter != null) {

            // get params
            boolean newPerson = intent.getBooleanExtra(PARAM_NEW_PERSON, true);
            int positionId = intent.getIntExtra(PARAM_POSITION_ID, 0);
            Person person = intent.getParcelableExtra(PARAM_PERSON);

            // if a new person was added, add to adapter, otherwise find the positionId and update
            if (newPerson) {
                mPersonsAdapter.add(person);
            } else {
                Person clickedPerson = (Person) mPersonsAdapter.getItem(positionId);
                clickedPerson.setName(person.getName());
                clickedPerson.setSubtotal(person.getSubtotal());
                mPersonsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(PARAM_PERSONS_ARRAY, getPersonsFromAdapter(mPersonsAdapter));
        super.onSaveInstanceState(outState);
    }

    private Person[] getPersonsFromAdapter(PersonAdapter adapter) {
        Person[] persons = new Person[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {
            persons[i] = (Person) adapter.getItem(i);
            Log.v("AllocateFragment", "Name of Person parceled: " + persons[i].getName());
        }
        return persons;
    }

    @Override
    public void onDestroy() {
        Log.v("AllocateFragment", "Activity destroyed");
        super.onDestroy();
    }
}
