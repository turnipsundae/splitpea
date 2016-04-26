package com.laudev.android.splitpea;

import android.content.Intent;
import android.support.annotation.Nullable;
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
    private String name;
    private float subtotal;
    private float total;
    private float subtotalPerson;
    private float totalPerson;
    private List<Person> summaryList;

    private final String PARAM_NEW_EVENT = "newEvent";
    private final String PARAM_NEW_PERSON = "newPerson";
    private final String PARAM_POSITION_ID = "positionId";
    private final String PARAM_NAME = "name";
    private final String PARAM_SUBTOTAL = "subtotal";
    private final String PARAM_TOTAL = "total";
    private final String PARAM_SUBTOTAL_PERSON = "subtotalPerson";
    private final String PARAM_TOTAL_PERSON = "totalPerson";
    private final String PARAM_PERSON = "person";
    private final String PARAM_PERSONS_ARRAY = "persons";

    private View mSubtotalFooterView;
    private View mTotalFooterView;

    private PersonAdapter mPersonsAdapter;

    public AllocateFragment() {
    }

    @Override
    public void onResume() {
        Log.v("AllocateFragment", "onResume called");

        // get the intent
        Intent intent = getActivity().getIntent();

        updatePersonsWithIntent(intent);

        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("AllocateFragment", "onCreate called");
        if (savedInstanceState != null) {
            Person[] persons = (Person[]) savedInstanceState.getParcelableArray("Persons");
            summaryList = new ArrayList<>(Arrays.asList(persons));
            String names = "";
            if (persons != null) {
                for (Person person : persons) {
                    names += person.getName() + " ";
                }
            }
            Log.v("AllocateFragment", names);
        } else {
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

            if (summaryList != null) {
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
                        if (person != null) {
                            startActivity(new Intent(getActivity(), DetailActivity.class)
                                    .putExtra(PARAM_NEW_PERSON, false)
                                    .putExtra(PARAM_POSITION_ID, position)
                                    .putExtra(PARAM_PERSON, person));
                        }
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
