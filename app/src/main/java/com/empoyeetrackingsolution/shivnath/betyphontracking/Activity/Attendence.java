package com.empoyeetrackingsolution.shivnath.betyphontracking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter.AttendenceAdapter;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.AttendenceModel;

import io.realm.Realm;
import io.realm.RealmResults;

public class Attendence extends AppCompatActivity {

    RecyclerView recyclerView;
    private AttendenceAdapter adapter;
    private RealmResults<AttendenceModel> incpmingCallList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        recyclerView = findViewById(R.id.attenRecycler);


        incpmingCallList = Realm.getDefaultInstance().where(AttendenceModel.class).findAllAsync();
        adapter = new AttendenceAdapter(getApplicationContext(),incpmingCallList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
