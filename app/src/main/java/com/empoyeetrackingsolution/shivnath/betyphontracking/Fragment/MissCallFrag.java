package com.empoyeetrackingsolution.shivnath.betyphontracking.Fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter.MissCallLogAdapter;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Miss_Detect;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissCallFrag extends Fragment {

    private RecyclerView recyclerView;
    private MissCallLogAdapter adapter;
    private RealmResults<Miss_Detect> miss_detects;



    public MissCallFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_miss_call, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        recyclerView= view.findViewById(R.id.ms_recycler);
        miss_detects = Realm.getDefaultInstance().where(Miss_Detect.class).findAllAsync();
        adapter = new MissCallLogAdapter(getActivity(), miss_detects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance(); //creating  database oject
        RealmResults<Miss_Detect> results = realm.where(Miss_Detect.class).findAllAsync();
        //fetching the data
        results.load();
        for(Miss_Detect information:results){
            System.out.println("checing"+information.getMissNum());
        }

    }

}
