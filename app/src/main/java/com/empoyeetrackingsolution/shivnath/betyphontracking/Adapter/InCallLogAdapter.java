package com.empoyeetrackingsolution.shivnath.betyphontracking.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empoyeetrackingsolution.shivnath.betyphontracking.Activity.CallRecoding;
import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Incoming_Detect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class InCallLogAdapter extends RealmRecyclerViewAdapter<Incoming_Detect, InCallLogAdapter.MyViewHolder> {

    private Context context;
        static MediaPlayer mp;

    private ListView listview;
    private String mediaPath;
    public static List<String> songs = new ArrayList<String>();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private InCallLogAdapter.SongsLoaderAsyncTask task;
    ProgressBar songsLoadingProgressBar;
    ArrayList<String> songNames = new ArrayList<>();
    private RecyclerView recyclerView;
    ProgressDialog progressBar;


    private class SongsLoaderAsyncTask extends AsyncTask<Void, String, Void> {
        private List<String> loadedSongs = new ArrayList<String>();

        /**
         * `
         * Before our background starts
         */
        protected void onPreExecute() {
            songsLoadingProgressBar.setVisibility(View.VISIBLE);
            songNames.clear();
            Toast.makeText(context, "Scanning Songs..Please Wait.", Toast.LENGTH_LONG).show();
        }

        /**
         * Load files in background thread here
         *
         * @param url
         * @return
         */
        protected Void doInBackground(Void... url) {
            updateSongListRecursive(new File(mediaPath));
            return null;
        }

        /**
         * Recursively Load Files From ExternalStorage
         *
         * @param path
         */
        public void updateSongListRecursive(File path) {
            if (path.isDirectory()) {
                for (int i = 0; i < path.listFiles().length; i++) {
                    File file = path.listFiles()[i];
                    updateSongListRecursive(file);

                }
            }else {
                String songPath = path.getAbsolutePath();
                String songName = path.getName();
                publishProgress(songPath);
                if (songPath.endsWith(".amr")) {
                    loadedSongs.add(songPath);
                    songNames.add(songName.substring(0, songName.length() - 4));
                }

            }
        }

        protected void onPostExecute(Void args) {
            songsLoadingProgressBar.setVisibility(View.GONE);
//            ArrayAdapter<String> songList = new ArrayAdapter<String>(CallRecoding.this, android.R.layout.simple_list_item_1, songNames);
//            listview.setAdapter(songList);

            //Settting data on recyler view

//            RecyclerViewAdapter adapter2 = new RecyclerViewAdapter(songNames, getApplicationContext());
//            recyclerView.setAdapter(adapter2);
//            LinearLayoutManager llm = new LinearLayoutManager(CallRecoding.this);
//            llm.setOrientation(LinearLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(llm);
//            songs = loadedSongs;
//            Toast.makeText(getApplicationContext(), "Scanning Complete." + songs.size() + " Recoding Found.", Toast.LENGTH_LONG).show();
        }
    }


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

        holder.playFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mediaPath = Environment.getExternalStorageDirectory().getPath() + "/ETS/";
//                task = new InCallLogAdapter.SongsLoaderAsyncTask();
//                task.execute();
//
//                System.out.println("ghghy"+mediaPath);
            }
        });



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



//                    pause.setVisibility(View.VISIBLE);
//                    playFile.setVisibility(View.INVISIBLE);
//
//                     mp=new MediaPlayer();
//                    try{
//                        //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
//                        mp.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/Download/Bom Diggy Diggy.mp3");
//
//                        mp.prepare();
//                    }catch(Exception e){e.printStackTrace();}
//
//                    mp.start();

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
