package com.example.shivnath.betyphontracking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shivnath.betyphontracking.R;
import com.example.shivnath.betyphontracking.model.Incoming_Detect;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class InCallLogAdapter extends RealmRecyclerViewAdapter<Incoming_Detect, InCallLogAdapter.MyViewHolder> {

    private Context context;

    public InCallLogAdapter(Context context, RealmResults<Incoming_Detect> data ) {
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
    public void onBindViewHolder(@NonNull final InCallLogAdapter.MyViewHolder holder, final int position) {

        final Incoming_Detect object = getData().get(position);

        holder.num.setText("Phone Number :- "+object.getNum());
        holder.inDate.setText("Date :- "+object.getIncomingDate());


    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView num,inDate;
        private ImageButton playFile;

        public MyViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.tv_number);
            inDate = itemView.findViewById(R.id.tv_date);
            playFile = itemView.findViewById(R.id.ic_play);

            playFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    _counter++;
//                    _stringVal = Integer.toString(_counter);
//
//                    Toast.makeText(context, _stringVal, Toast.LENGTH_SHORT).show();

                }
            });

        }
    }


}
