package com.laudev.android.splitpea;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllocateFragment extends Fragment {

    private Total mEventTotal;
    private List<Person> summaryList;

    private final String PARAM_NEW_EVENT = "newEvent";
    private final String PARAM_NEW_PERSON = "newPerson";
    private final String PARAM_POSITION_ID = "positionId";
    private final String PARAM_PERSON = "person";
    private final String PARAM_PERSONS_ARRAY = "persons";
    private final String PARAM_EVENT_TOTAL = "mEventTotal";
    private final int PERSON_DETAIL_REQUEST = 1;
    private final int ADD_PERSON_REQUEST = 2;

    private TextView mFooterSubtotalValue;
    private TextView mFooterTotalValue;

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
                    updateFooterViews();
                }
                break;
            case ADD_PERSON_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Person person = data.getParcelableExtra(PARAM_PERSON);
                    mPersonsAdapter.add(person);
                    updateFooterViews();
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
            if (intent.hasExtra(PARAM_NEW_EVENT) && intent.hasExtra(PARAM_EVENT_TOTAL)) {
                mEventTotal = intent.getParcelableExtra(PARAM_EVENT_TOTAL);
                float mSubtotal = mEventTotal.getSubtotal();
                float mTotal = mEventTotal.getTotal();
                Log.v("AllocateFragment", "Subtotal/Total: " + mSubtotal + "/" + mTotal);
            }

            // initialize summaryList with no data
            summaryList = new ArrayList<>();
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

        // add three footers for subtotal, tax + tip, total
        addFooterViews(getContext(), personsListView, R.layout.listview_item_footer, mEventTotal);

        // set click listener
        personsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (view.getId()) {
                    // if existing person clicked, launch detail activity
                    case (R.id.listview_item_person):
                        Person person = (Person) mPersonsAdapter.getItem(position);
                        startActivityForResult(new Intent(getActivity(), DetailActivity.class)
                                        .putExtra(PARAM_NEW_PERSON, false)
                                        .putExtra(PARAM_POSITION_ID, position)
                                        .putExtra(PARAM_PERSON, person),
                                PERSON_DETAIL_REQUEST);
                        break;

                    // if footer clicked, launch main activity
                    case (R.id.listview_item_footer):
                        Toast.makeText(getActivity(), "You clicked on a footer", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // find FAB from parent
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Person newPerson = new Person();
                    newPerson.setTax(mEventTotal.getTax());
                    newPerson.setTip(mEventTotal.getTip());
                    startActivityForResult(new Intent(getActivity(), DetailActivity.class)
                            .putExtra(PARAM_NEW_PERSON, true)
                            .putExtra(PARAM_PERSON, newPerson),
                            ADD_PERSON_REQUEST);
                }
            });
        }

        return rootView;
    }

    private void updateFooterViews() {
        // get subtotals
        float subtotal = getSubtotals();
        mEventTotal.updateSubtotalRemainder(subtotal);
        if (mFooterSubtotalValue != null) {
            mFooterSubtotalValue.setText(this.getString(R.string.format_accounted_vs_original,
                    subtotal, mEventTotal.getSubtotal()));
        }

        // get totals
        float total = getTotals();
        if (mFooterTotalValue != null) {
            mFooterTotalValue.setText(this.getString(R.string.format_accounted_vs_original,
                    total, mEventTotal.getTotal()));
        }
    }

    // update listview footer with eventTotal
    private void addFooterViews(Context context, ListView listView, int resLayoutId, Total eventTotal) {
        // get inflater and inflate footer view
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate subtotal
        View footerSubtotalView = inflater.inflate(resLayoutId, null, false);
        TextView subtotalName = (TextView) footerSubtotalView.findViewById(R.id.name_textview);
        subtotalName.setText(getString(R.string.subtotal));
        mFooterSubtotalValue = (TextView) footerSubtotalView.findViewById(R.id.subtotal_textview);
        mFooterSubtotalValue.setText(this.getString(R.string.format_accounted_vs_original,
                getSubtotals(), mEventTotal.getSubtotal()));
        listView.addFooterView(footerSubtotalView);

        // inflate tax
        View footerTaxView = inflater.inflate(resLayoutId, null, false);
        TextView taxName = (TextView) footerTaxView.findViewById(R.id.name_textview);
        taxName.setText(getString(R.string.tax));
        TextView taxValue = (TextView) footerTaxView.findViewById(R.id.subtotal_textview);
        taxValue.setText(this.getString(R.string.format_tax_tip, mEventTotal.getTax()));
        listView.addFooterView(footerTaxView);

        // inflate total
        View footerTotalView = inflater.inflate(resLayoutId, null, false);
        TextView totalName = (TextView) footerTotalView.findViewById(R.id.name_textview);
        totalName.setText(getString(R.string.total));
        mFooterTotalValue = (TextView) footerTotalView.findViewById(R.id.subtotal_textview);
        mFooterTotalValue.setText(this.getString(R.string.format_accounted_vs_original,
                getTotals(), mEventTotal.getTotal()));
        listView.addFooterView(footerTotalView);
    }

    private float getTotals() {
        float sum = 0f;
        if (summaryList != null) {
            for (Person person : summaryList) {
                person.updateTotal();
                sum += person.getTotal();
            }
        }
        return sum;
    }

    private float getSubtotals() {
        float sum = 0f;
        if (summaryList != null) {
            for (Person person : summaryList) {
                sum += person.getSubtotal();
            }
        }
        return sum;
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
