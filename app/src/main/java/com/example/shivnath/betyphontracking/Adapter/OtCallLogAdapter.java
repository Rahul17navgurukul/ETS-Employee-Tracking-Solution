package com.example.shivnath.betyphontracking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shivnath.betyphontracking.R;
import com.example.shivnath.betyphontracking.model.Incoming_Detect;
import com.example.shivnath.betyphontracking.model.Outgoing_Detect;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class OtCallLogAdapter extends RealmRecyclerViewAdapter<Outgoing_Detect, OtCallLogAdapter.MyViewHolder> {

    private Context context;

    public OtCallLogAdapter(Context context, OrderedRealmCollection<Outgoing_Detect>data ) {
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
    public void onBindViewHolder(@NonNull final OtCallLogAdapter.MyViewHolder holder, final int position) {

        final Outgoing_Detect object = getData().get(position);

        holder.num.setText("Phone Number :- "+object.getOutNumer());
        holder.inDate.setText("Date :- "+object.getOutDate());


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
