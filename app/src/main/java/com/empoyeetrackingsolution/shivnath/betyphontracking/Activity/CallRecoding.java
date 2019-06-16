package com.empoyeetrackingsolution.shivnath.betyphontracking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.empoyeetrackingsolution.shivnath.betyphontracking.R;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.Incoming_Detect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CallRecoding extends AppCompatActivity {

    private ListView listview;
    private String mediaPath;
    public static List<String> songs = new ArrayList<String>();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SongsLoaderAsyncTask task;
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
            Toast.makeText(getApplicationContext(), "Scanning Songs..Please Wait.", Toast.LENGTH_LONG).show();
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

            RecyclerViewAdapter adapter2 = new RecyclerViewAdapter(songNames, getApplicationContext());
            recyclerView.setAdapter(adapter2);
            LinearLayoutManager llm = new LinearLayoutManager(CallRecoding.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            songs = loadedSongs;
            Toast.makeText(getApplicationContext(), "Scanning Complete." + songs.size() + " Recoding Found.", Toast.LENGTH_LONG).show();
        }
    }

    private void openPlayerActivity(int position) {
//        Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
//        i.putExtra("SONG_KEY", songs.get(position));
//        startActivity(i);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_recoding);


        songsLoadingProgressBar = findViewById(R.id.myProgressBar);
//        listview = findViewById(R.id.mListView);
        recyclerView = findViewById(R.id.recordlist);
        mediaPath = Environment.getExternalStorageDirectory().getPath() + "/ETS/";
        task = new SongsLoaderAsyncTask();
        task.execute();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) mediaPlayer.reset();
    }


}

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public ArrayList<String> songNames;
    Context context;
    //    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean wasPlaying = false;
    Handler seekHandler = new Handler();
    Runnable run;


    public RecyclerViewAdapter(ArrayList<String> songNames, Context context) {
        this.songNames = songNames;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_card, parent, false);
        return new MyViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.MyViewHolder holder, final int position) {

        holder.textView.setText(songNames.get(position));


        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.playerCard.setVisibility(View.VISIBLE);
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    Toast.makeText(context, "net work", Toast.LENGTH_SHORT).show();
                    connected = true;

                    postingData(position, holder);
                    System.out.println("Something");
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();

                    if (connected == true ) {

                    }

                }
                else{

                    connected = false;
                    Toast.makeText(context, "net not", Toast.LENGTH_SHORT).show();

                }

            }
        });

        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.playerCard.setVisibility(View.VISIBLE);
            }
        });

        final MediaPlayer mediaPlayer = new MediaPlayer();



        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(CallRecoding.songs.get(position));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        holder.seekBar.setMax(mediaPlayer.getDuration());
        holder.seekBar.setTag(position);

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        holder.seekBarHint.setText("0:00/" + calculateDuration(mediaPlayer.getDuration()));

        holder.playsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    holder.pauseRec.setVisibility(View.VISIBLE);
                    holder.playsong.setVisibility(View.INVISIBLE);
                    holder.playsong.setText(" Pause ");
                    run = new Runnable() {
                        @Override
                        public void run() {
                            // Updateing SeekBar every 100 miliseconds
                            holder.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            seekHandler.postDelayed(run, 100);
                            //For Showing time of audio(inside runnable)
                            int miliSeconds = mediaPlayer.getCurrentPosition();
                            if (miliSeconds != 0) {
                                //if audio is playing, showing current time;
                                long minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds);
                                long seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                                if (minutes == 0) {
                                    holder.seekBarHint.setText("0:" + seconds + "/" + calculateDuration(mediaPlayer.getDuration()));
                                } else {
                                    if (seconds >= 60) {
                                        long sec = seconds - (minutes * 60);
                                        holder.seekBarHint.setText(minutes + ":" + sec + "/" + calculateDuration(mediaPlayer.getDuration()));
                                    }
                                }
                            } else {
                                //Displaying total time if audio not playing
                                int totalTime = mediaPlayer.getDuration();
                                long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime);
                                long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
                                if (minutes == 0) {
                                    holder.seekBarHint.setText("0:" + seconds);
                                } else {
                                    if (seconds >= 60) {
                                        long sec = seconds - (minutes * 60);
                                        holder.seekBarHint.setText(minutes + ":" + sec);
                                    }
                                }
                            }
                        }

                    };
                    run.run();
                } else {
                    mediaPlayer.pause();
                    holder.pauseRec.setVisibility(View.INVISIBLE);
                    holder.playsong.setVisibility(View.VISIBLE);
                    holder.playsong.setText(" Play ");
                }
            }
        });


        holder.pauseRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.pause();
                holder.playsong.setVisibility(View.VISIBLE);
                holder.pauseRec.setVisibility(View.GONE);
            }
        });


    }


    private void postingData(final int position, final MyViewHolder holder) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String value = preferences.getString("user_id", "0");
        String currentTime = preferences.getString("currentDate", "1");
        String callType = preferences.getString("recodingType","0");
        String recodingNumber = preferences.getString("recodingNumber","1");

        System.out.println("recodingNumer"+recodingNumber);


        Realm.init(context);
        Realm realm = Realm.getDefaultInstance(); //creating  database oject
        RealmResults<Incoming_Detect> results = realm.where(Incoming_Detect.class).findAllAsync();


        String[] arrayString = songNames.get(position).split("_");

        System.out.println("hjhjhjgj"+arrayString);

        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", songNames.get(position),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(CallRecoding.songs.get(position))))

                .addFormDataPart("user_id", value)
                .addFormDataPart("file_name", "Call Recording")
                .addFormDataPart("creation_datetime", currentTime)
                .addFormDataPart("prospect_number","null")
                .addFormDataPart("call_type","null")
                .build();

        Request request = new Request.Builder()
                .url("http://159.65.145.32/api/file/upload/")
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String json = response.body().string();
                    System.out.println("FileUploading >>> " + json);

                    run = new Runnable() {
                        @Override
                        public void run() {



                        }
                    };


                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return songNames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView, seekBarHint;
        TextView playsong, pauseRec;
        public static SeekBar seekBar;
        CardView playerCard, cardView1;
        ImageView recdonpost,notdone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.card_item);
            playsong = itemView.findViewById(R.id.recplay);
            seekBarHint = itemView.findViewById(R.id.songDur);
            seekBar = itemView.findViewById(R.id.seekbar);
            playerCard = itemView.findViewById(R.id.playerCard);
            cardView1 = itemView.findViewById(R.id.layout1);
            pauseRec = itemView.findViewById(R.id.recPause);
//            recdonpost = itemView.findViewById(R.id.recDonPost);
//            notdone = itemView.findViewById(R.id.recnotPost);

        }


    }

    private String calculateDuration(int duration) {
        String finalDuration = "";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        if (minutes == 0) {
            finalDuration = "0:" + seconds;
        } else {
            if (seconds >= 60) {
                long sec = seconds - (minutes * 60);
                finalDuration = minutes + ":" + sec;
            }
        }
        return finalDuration;
    }


}