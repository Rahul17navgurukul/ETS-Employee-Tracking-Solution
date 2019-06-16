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

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class AttendenceAdapter extends RealmRecyclerViewAdapter<AttendenceModel,AttendenceAdapter.MyViewHolder> {

    Context context;

    public AttendenceAdapter(Context context, RealmResults<AttendenceModel> data ) {
        super(data, true);
        this.context = context;
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

        final AttendenceModel object = getData().get(position);

        holder.onTime.setText(object.getOnDutyTime());
        holder.attenDate.setText(object.getSartDate());
        holder.offTime.setText(object.getOffDutyTime());

        if (object.getOffDutyTime() == null){

            holder.off.setVisibility(View.GONE);
            holder.offTime.setVisibility(View.GONE);

        }



    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView onTime,offTime,attenDate,off;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            onTime = itemView.findViewById(R.id.onTime);
            offTime = itemView.findViewById(R.id.offTime);
            attenDate = itemView.findViewById(R.id.atten_Date);
            off = itemView.findViewById(R.id.off);
        }
    }
}
