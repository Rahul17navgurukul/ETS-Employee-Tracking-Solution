package com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Outgoing_Detect;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class OtCallLogAdapter extends RealmRecyclerViewAdapter<Outgoing_Detect, OtCallLogAdapter.MyViewHolder> {

    private Context context;
    private List<String> songs = new ArrayList<String>();

    private String mediaPath;
    ArrayList<String> songNames = new ArrayList<>();


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
        private ImageButton play;

        public MyViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.tv_number);
            inDate = itemView.findViewById(R.id.tv_date);
            play = itemView.findViewById(R.id.ic_play);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final MediaPlayer mp=new MediaPlayer();
                    try{
                        //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                        mp.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/Download/Bom Diggy Diggy.mp3");

                        mp.prepare();
                    }catch(Exception e){e.printStackTrace();}

                    mp.start();


//
//
                }
            });

        }
    }


}
