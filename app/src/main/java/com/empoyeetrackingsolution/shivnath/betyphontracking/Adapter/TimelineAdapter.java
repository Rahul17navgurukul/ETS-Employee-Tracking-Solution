package com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.TimelineModel;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private Context context;
    private List<TimelineModel> modelList;

    public TimelineAdapter(Context context, List<TimelineModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public TimelineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_timeline, parent, false);
            return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.ViewHolder holder, int position) {

        TimelineModel object = modelList.get(position);

//        String date = object.getDateTime();
//        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss" );
//        String startDate = sdf2.format(date);



        holder.locDate.setText(object.getDateTime());
        holder.lonngitude.setText(object.getLongitute() +" & "+ object.getLatitude());
        holder.userId.setText(object.getUser_id());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lonngitude,userId,locDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lonngitude = itemView.findViewById(R.id.timelinelongitute);
            userId = itemView.findViewById(R.id.locUserId);
            locDate= itemView.findViewById(R.id.locDate);

        }
    }
}
