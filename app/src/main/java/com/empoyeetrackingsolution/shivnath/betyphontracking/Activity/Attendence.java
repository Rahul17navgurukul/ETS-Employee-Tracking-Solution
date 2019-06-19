package com.empoyeetrackingsolution.shivnath.betyphontracking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter.AttendenceAdapter;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.AttendenceModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Attendence extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    private AttendenceAdapter adapter;
    private RealmResults<AttendenceModel> incpmingCallList;
    Spinner sp1,sp2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);


        sp1 = findViewById(R.id.sp1);
        sp2 = findViewById(R.id.sp2);
        sp1.setOnItemSelectedListener(this);
        sp2.setOnItemSelectedListener(this);
        final CardView cardView = findViewById(R.id.filetrOpt);
        final ImageView imageView = findViewById(R.id.rec_filter);
        final ImageView imageView1 = findViewById(R.id.rec_unfilter);

        imageView1.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.INVISIBLE);
            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Today");
        categories.add("Yesterday");
        categories.add("Last 7 days");
        categories.add("Last 10 days");
        categories.add("Last 30 days");
        categories.add("This Month");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Attendence.this, android.
                R.layout.simple_spinner_item, categories);

        List<String> categories2 = new ArrayList<String>();
        categories2.add("Select User");
        categories2.add("User 1");
        categories2.add("User 2");
        categories2.add("User 3");
        categories2.add("User 4");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Attendence.this,
                android.R.layout.simple_spinner_item, categories2);

        sp1.setAdapter(dataAdapter);
        sp2.setAdapter(dataAdapter2);

        recyclerView = findViewById(R.id.attenRecycler);
        incpmingCallList = Realm.getDefaultInstance().where(AttendenceModel.class).findAllAsync();
        adapter = new AttendenceAdapter(getApplicationContext(),incpmingCallList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
