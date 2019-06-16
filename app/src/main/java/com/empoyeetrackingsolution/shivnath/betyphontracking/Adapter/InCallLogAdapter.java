package com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Incoming_Detect;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class InCallLogAdapter extends RealmRecyclerViewAdapter<Incoming_Detect, InCallLogAdapter.MyViewHolder> {

    private Context context;
    static MediaPlayer mp;

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

        Incoming_Detect user = Realm.getDefaultInstance().where(Incoming_Detect.class).findFirst();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("userName", "0");
        holder.iniby.setText("Initiated By :-    "+value);

        holder.num.setText("Phone Number :- "+object.getNum());
        holder.inDate.setText("Date :- "+object.getIncomingDate());


    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView num,inDate,iniby;
        private ImageView playFile,pause;
        public static ImageView check,done;

        public MyViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.tv_number);
            inDate = itemView.findViewById(R.id.tv_date);
            playFile = itemView.findViewById(R.id.ic_play);
            pause = itemView.findViewById(R.id.ic_pause);
            check = itemView.findViewById(R.id.ic_check);
            done = itemView.findViewById(R.id.ic_done);
            iniby = (TextView) itemView.findViewById(R.id.tv_initiated);

            done.setVisibility(View.GONE);

            playFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pause.setVisibility(View.VISIBLE);
                    playFile.setVisibility(View.INVISIBLE);

                     mp=new MediaPlayer();
                    try{
                        //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                        mp.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/Download/Bom Diggy Diggy.mp3");

                        mp.prepare();
                    }catch(Exception e){e.printStackTrace();}

                    mp.start();

                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pause.setVisibility(View.INVISIBLE);
                    playFile.setVisibility(View.VISIBLE);
                    mp.stop();

                }
            });
        }


    }




}
