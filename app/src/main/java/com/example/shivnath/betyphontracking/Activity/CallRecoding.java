package com.example.shivnath.betyphontracking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shivnath.betyphontracking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CallRecoding extends AppCompatActivity {

    private ListView listview;
    private String mediaPath;
    private List<String> songs = new ArrayList<String>();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SongsLoaderAsyncTask task;
    ProgressBar songsLoadingProgressBar;
    ArrayList<String> songNames = new ArrayList<>();
    private RecyclerView recyclerView;

    String recPath,recName;

    private class SongsLoaderAsyncTask extends AsyncTask<Void, String, Void> {
        private List<String> loadedSongs = new ArrayList<String>();

        /**
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
            } else {
                String songPath = path.getAbsolutePath();
                String songName = path.getName();
                publishProgress(songPath);
                if (songPath.endsWith(".mp3")) {
                    loadedSongs.add(songPath);
                    songNames.add(songName.substring(0, songName.length() - 4));
                }
            }
        }

        protected void onPostExecute(Void args) {
            songsLoadingProgressBar.setVisibility(View.GONE);
            ArrayAdapter<String> songList = new ArrayAdapter<String>(CallRecoding.this, android.R.layout.simple_list_item_1, songNames);
            listview.setAdapter(songList);
            songs = loadedSongs;
            Toast.makeText(getApplicationContext(), "Scanning Complete." + songs.size() + " Recoding Found.", Toast.LENGTH_LONG).show();
        }
    }

    private void openPlayerActivity(int position) {
        Intent i = new Intent(this, PlayerActivity.class);
        i.putExtra("SONG_KEY", songs.get(position));
        startActivity(i);
        postingData(position);

        System.out.println("songType"+songs);
        System.out.println("soneName"+songNames);

        recName = songNames.get(position);
        recPath = songs.get(position);

    }

    private void postingData(int position) {

        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id","1");
            jsonObject.put("file",recPath);
            jsonObject.put("file_name",recName);
            jsonObject.put("creation_datetime","2019-05-27T10:04:16.040757Z");




        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
//
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
//        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"user_id\"\r\n\r\n1\r\n-" +
//                "-----WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: " +
//                "form-data; name=\"file\"; filename=\"demo.png\"\r\nContent" +
//                "-Type: image/png\r\n\r\n\r\n------WebKitFormBoundary" +
//                "7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
//                "name=\"file_name\"\r\n\r\ncall recording\r\n------" +
//                "WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data;" +
//                " name=\"creation_datetime\"\r\n\r\n2019-05-27T10:04:16.040757Z\r\n------" +
//                "WebKitFormBoundary7MA4YWxkTrZu0gW--");

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());


        Request request = new Request.Builder()
                .url("http://159.65.145.32/api/file/upload/")
                .post(body)
                .build();

//        Response response = client.newCall(request).execute();

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
                    System.out.println("File Uploading" + json);

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_recoding);

        songsLoadingProgressBar = findViewById(R.id.myProgressBar);
        listview = findViewById(R.id.mListView);
//        recyclerView = findViewById(R.id.callRecodingRecycler);
        mediaPath = Environment.getExternalStorageDirectory().getPath() + "/Betyphon/";
//        mediaPath = Environment.getExternalStorageDirectory().getPath() + "%Download%";

        //mediaPath = Environment.getExternalStorageDirectory().getPath() + "/mnt/shared/Other/";
        //mediaPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath() ;
        //mediaPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() ;
        // itemclick listener for our listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPlayerActivity(position);
            }
        });
        //instantiate and execute our asynctask
        task = new SongsLoaderAsyncTask();
        task.execute();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) mediaPlayer.reset();
    }
}
