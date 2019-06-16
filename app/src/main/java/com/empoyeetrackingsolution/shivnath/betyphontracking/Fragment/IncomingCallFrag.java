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

import com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter.InCallLogAdapter;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Incoming_Detect;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class IncomingCallFrag extends Fragment {

    private RecyclerView recyclerView;
    private InCallLogAdapter adapter;
    private RealmResults<Incoming_Detect> incpmingCallList;

    public IncomingCallFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_incoming_call, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        recyclerView= view.findViewById(R.id.recycler);
        incpmingCallList = Realm.getDefaultInstance().where(Incoming_Detect.class).findAllAsync();
        adapter = new InCallLogAdapter(getActivity(), incpmingCallList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance(); //creating  database oject
        RealmResults<Incoming_Detect> results = realm.where(Incoming_Detect.class).findAllAsync();
        //fetching the data
        results.load();
        for(Incoming_Detect information:results){
        }

    }



}
