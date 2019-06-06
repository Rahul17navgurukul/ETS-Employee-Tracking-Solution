package com.example.shivnath.betyphontracking.Fragment;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shivnath.betyphontracking.Adapter.InCallLogAdapter;
import com.example.shivnath.betyphontracking.Adapter.OtCallLogAdapter;
import com.example.shivnath.betyphontracking.R;
import com.example.shivnath.betyphontracking.model.Incoming_Detect;
import com.example.shivnath.betyphontracking.model.Outgoing_Detect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutgoingCallFrag extends Fragment {

    private RecyclerView recyclerView;
    private OtCallLogAdapter adapter;
    private RealmResults<Outgoing_Detect> outgoing_detects;
    private RealmResults<Incoming_Detect> incpmingCallList;



    public OutgoingCallFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outgoing_call, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getContext());
        pref.edit().putInt("numOfCalls",0).apply();



        recyclerView = view.findViewById(R.id.ot_recycler);
        outgoing_detects = Realm.getDefaultInstance().where(Outgoing_Detect.class).findAllAsync();
        adapter = new OtCallLogAdapter(getActivity(), outgoing_detects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance(); //creating  database oject
        RealmResults<Outgoing_Detect> results = realm.where(Outgoing_Detect.class).findAllAsync();
        //fetching the data
        results.load();
        for(Outgoing_Detect information:results){
            System.out.println("checing"+information.getOutDate());
        }

    }





}