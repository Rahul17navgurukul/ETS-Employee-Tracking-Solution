package com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.MyTeamModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.TimelineModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyTeamAdapter extends RecyclerView.Adapter<MyTeamAdapter.MyViewHolder> implements AdapterView.OnItemSelectedListener {

    private Context context;
//    private List<MyTeamModel> myTeamModelList;
    private List<String> mData;

    public MyTeamAdapter(Context context, List<String> mData) {
        this.context = context;
        this.mData = mData;
    }
//    public MyTeamAdapter(Context context, List<MyTeamModel> myTeamModelList) {
//        this.context = context;
//        this.myTeamModelList = myTeamModelList;
//    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_my_team, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        MyTeamModel myTeamModel = myTeamModelList.get(position);
        String name = mData.get(position);

        holder.userName.setText(name);
        holder.spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Tele Caller");
        categories.add("Field Executive");
        categories.add("Manager");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories);

        holder.spinner.setAdapter(dataAdapter);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        Spinner spinner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            spinner = itemView.findViewById(R.id.spinner);


        }

    }
}
