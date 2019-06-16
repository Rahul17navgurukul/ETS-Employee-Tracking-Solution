package com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Miss_Detect;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class MissCallLogAdapter extends RealmRecyclerViewAdapter<Miss_Detect, MissCallLogAdapter.MyViewHolder> {

    private Context context;

    public MissCallLogAdapter(Context context, OrderedRealmCollection<Miss_Detect>data ) {
        super(data, true);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_call_log_list_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MissCallLogAdapter.MyViewHolder holder, final int position) {

        final Miss_Detect object = getData().get(position);

        holder.num.setText("Phone Number :- "+object.getMissNum());
        holder.inDate.setText("Date :- "+object.getMissDate());


    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView num,inDate;

        public MyViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.tv_number);
            inDate = itemView.findViewById(R.id.tv_date);

        }
    }


}
