package com.laudev.android.splitpea;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AllocateActivity extends AppCompatActivity {

    private final String PARAM_NEW_PERSON = "newPerson";
    private final int ADD_PERSON_REQUEST = 2;
    private final String ALLOCATE_FRAGMENT_TAG = "AllocateFragmentTag";

    private boolean newPerson = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new AllocateFragment(), ALLOCATE_FRAGMENT_TAG)
                .commit();
    }

}
