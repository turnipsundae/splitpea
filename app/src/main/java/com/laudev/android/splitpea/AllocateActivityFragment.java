package com.laudev.android.splitpea;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllocateActivityFragment extends Fragment {

    private float subtotal;
    private float total;

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
        TextView totalTextView = (TextView)rootView.findViewById(R.id.total);
        totalTextView.setText("Subtotal is " + subtotal + "\n" + "Total is " + total);
        return rootView;
    }
}
