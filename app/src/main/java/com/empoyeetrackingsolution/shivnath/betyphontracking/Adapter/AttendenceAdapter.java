package com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.AttendenceModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.TimelineModel;

import java.text.SimpleDateFormat;
import java.util.List;


public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.MyViewHolder> {

    Context context;
    private List<AttendenceModel> modelList;

    public AttendenceAdapter(Context context, List<AttendenceModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attence_custom, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final AttendenceModel object = modelList.get(position);

        holder.date.setText(object.getCreation_datetime());
        holder.name.setText(object.getFull_name());
        holder.logouttime.setText(object.getLogout_time());
        holder.logintime.setText(object.getLogin_time());
        holder.user.setText(object.getUser_id());
        holder.status.setText(object.getStatus());





    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,user,date,logintime,status,logouttime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            user = itemView.findViewById(R.id.userId);
            date = itemView.findViewById(R.id.atten_Date);
            logintime = itemView.findViewById(R.id.logintime);
            logouttime = itemView.findViewById(R.id.logouttime);
            status = itemView.findViewById(R.id.status_);
        }
    }
}
